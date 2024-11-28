package com.example.Spring_Study_Presigned_URL.S3

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class BucketService(
        @Value("\${aws.s3.bucket}")
        private val bucketName: String,
        private val s3Presigner: S3Presigner
        //jwt 정보도 추가
) {
    fun getPresigndUrlForUpload(objectName: String): String {

        val putObjectRequest = PutObjectRequest.builder() //업로드
                .bucket(bucketName)
                .key(objectName)
                .contentType("image/jpg") // 이미지 확장자를 여러 개로 정해야 할 듯.
                .build()

        val preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL 기한을 5분으로 설정
                .putObjectRequest(putObjectRequest)
                .build()

        return s3Presigner.presignPutObject(preSignRequest).url().toString()
    }

    fun getPresigndUrlForDownload(objectName: String): String {

        val getObjectRequest = GetObjectRequest.builder() //다운로드
                .bucket(bucketName)
                .key(objectName)
                .build()

        val preSignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60)) // URL 기한을 5분으로 설정
                .getObjectRequest(getObjectRequest)
                .build()

        return s3Presigner.presignGetObject(preSignRequest).url().toString()
    }

}