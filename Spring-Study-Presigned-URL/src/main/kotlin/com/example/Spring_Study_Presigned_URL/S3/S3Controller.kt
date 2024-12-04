package com.example.Spring_Study_Presigned_URL.S3

import com.example.Spring_Study_Presigned_URL.DTO.ResponseDTO
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadAbortRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadCompleteRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadInitiateRequest
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.S3UploadCreatePresignedUrlRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class S3Controller(
        private val s3Service: S3Service,
) {

    @PostMapping("/initiateUpload")
    fun initiateUpload(@RequestBody s3UploadInitiateRequest: S3UploadInitiateRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3Service.initiateUpload(s3UploadInitiateRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/presigned-url")
    fun createUploadPresignedUrl(@RequestBody s3UploadCreatePresignedUrlRequest: S3UploadCreatePresignedUrlRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3Service.createUploadPreSignedUrl(s3UploadCreatePresignedUrlRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/complete-upload")
    fun completeUpload(@RequestBody s3UploadCompleteRequest: S3UploadCompleteRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3Service.completeUpload(s3UploadCompleteRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }

    @PostMapping("/abort-upload")
    fun abortUpload(@RequestBody s3UploadAbortRequest: S3UploadAbortRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3Service.abortUpload(s3UploadAbortRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }
}