package com.example.Spring_Study_Presigned_URL.S3

import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadAbortRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadCompleteRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadInitiateRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadCreatePresignedUrlRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.response.S3UploadCompleteResponse
import com.example.Spring_Study_Presigned_URL.S3.DTO.response.S3UploadInitiateResponse
import com.example.Spring_Study_Presigned_URL.S3.DTO.response.S3UploadCreatePresignedUrlResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.AbortMultipartUploadResponse
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload
import software.amazon.awssdk.services.s3.model.CompletedPart
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.UploadPartRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PresignedUploadPartRequest
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest
import java.time.Duration

@Service
@Transactional
class S3Service(
        @Value("\${aws.s3.bucket}")
        private val bucketName: String,
        private val s3Client: S3Client,
        private val s3Presigner: S3Presigner
        //jwt 정보도 추가
) {

    fun initiateUpload(s3UploadInitiateRequest: S3UploadInitiateRequest): S3UploadInitiateResponse {

        //S3에 저장할 객체 키 생성 (현재 시간 + 원본 파일 이름)
        val targetObjectDir = "original/${System.currentTimeMillis()}_${s3UploadInitiateRequest.originalFileName}"

        //S3 객체 메타데이터 설정
        val metadata = mapOf(
                "originalFileName" to s3UploadInitiateRequest.originalFileName,
                "fileType" to s3UploadInitiateRequest.fileType,
                "fileSize" to s3UploadInitiateRequest.fileSize.toString()
        )

        //CreateMultipartUploadRequest 생성 (메타데이터 포함)
        val createMultipartUploadRequest: CreateMultipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(targetObjectDir)
                .metadata(metadata)
                .build()

        //S3에 멀티파트 업로드 초기화 요청
        val createMultipartUploadResponse: CreateMultipartUploadResponse = s3Client.createMultipartUpload(createMultipartUploadRequest)

        //응답 DTO 생성 및 반환
        return S3UploadInitiateResponse(
                uploadId = createMultipartUploadResponse.uploadId(),
                key = targetObjectDir
        )
    }

    fun createUploadPreSignedUrl(s3UploadCreatePresignedUrlRequest: S3UploadCreatePresignedUrlRequest): S3UploadCreatePresignedUrlResponse {

        //Presigned URL 기한 설정
        val expiration = Duration.ofMinutes(10)

        //S3에 Presigned URL 요청할 정보 저장
        val uploadPartRequest: UploadPartRequest = UploadPartRequest.builder()
                .bucket(bucketName)
                .key(s3UploadCreatePresignedUrlRequest.key)
                .uploadId(s3UploadCreatePresignedUrlRequest.uploadId)
                .partNumber(s3UploadCreatePresignedUrlRequest.partNumber)
                .build()

        //S3에 Presigned URL 요청
        val uploadPartPresignRequest: UploadPartPresignRequest = UploadPartPresignRequest.builder()
                .signatureDuration(expiration)
                .uploadPartRequest(uploadPartRequest)
                .build()

        //클라이언트에서 S3로 직접 업로드하기 위해 사용할 Presigned URL을 받는다.
        val presignedUploadPartRequest: PresignedUploadPartRequest = s3Presigner.presignUploadPart(uploadPartPresignRequest)

        //응답 DTO 생성 및 반환
        return S3UploadCreatePresignedUrlResponse(
                presignedUrl = presignedUploadPartRequest.url().toString(),
                uploadId = s3UploadCreatePresignedUrlRequest.uploadId,
                partNumber = s3UploadCreatePresignedUrlRequest.partNumber,
                expiration = expiration
        )
    }

    fun completeUpload(s3UploadCompleteRequest: S3UploadCompleteRequest): S3UploadCompleteResponse {

        val completedPart = s3UploadCompleteRequest.parts.map { partForm ->
            CompletedPart.builder()
                    .partNumber(partForm.partNumber)
                    .eTag(partForm.eTag)
                    .build()
        }

        val completedMultipartUpload: CompletedMultipartUpload = CompletedMultipartUpload.builder()
                .parts(completedPart)
                .build()

        val completeMultipartUploadRequest: CompleteMultipartUploadRequest = CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(s3UploadCompleteRequest.key)
                .uploadId(s3UploadCompleteRequest.uploadId)
                .multipartUpload(completedMultipartUpload)
                .build()

        val completeMultipartUploadResponse: CompleteMultipartUploadResponse = s3Client.completeMultipartUpload(completeMultipartUploadRequest)

        val objectKey: String = completeMultipartUploadResponse.key()
        val url: String = completeMultipartUploadResponse.location()
        val bucket: String = completeMultipartUploadResponse.bucket()
        val fileSize: Long = getFileSizeFromS3Url(bucket, objectKey)

        return S3UploadCompleteResponse(
                fileName = objectKey,
                url = url,
                fileSize = fileSize
        )
    }

    fun abortUpload(s3UploadAbortRequest: S3UploadAbortRequest) {

        val abortMultipartUploadRequest: AbortMultipartUploadRequest = AbortMultipartUploadRequest.builder()
                .bucket(bucketName)
                .key(s3UploadAbortRequest.key)
                .uploadId(s3UploadAbortRequest.uploadId)
                .build()

        s3Client.abortMultipartUpload(abortMultipartUploadRequest)
    }

    fun getFileSizeFromS3Url(bucketName: String, fileName: String): Long {
        val metadataRequest: HeadObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build()
        return s3Client.headObject(metadataRequest).contentLength()
    }


}

