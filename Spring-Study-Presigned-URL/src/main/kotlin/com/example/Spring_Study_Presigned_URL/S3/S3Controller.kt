package com.example.Spring_Study_Presigned_URL.S3

import com.example.Spring_Study_Presigned_URL.DTO.ResponseDTO
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.*
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
    fun getUploadPresignedUrl(@RequestBody s3UploadPresignedUrlRequest: S3UploadPresignedUrlRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3Service.getUploadPreSignedUrl(s3UploadPresignedUrlRequest)
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

    @DeleteMapping("/delete")
    fun delete(@RequestBody s3DeleteRequest: S3DeleteRequest): ResponseEntity<ResponseDTO> {

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = s3Service.deleteObject(s3DeleteRequest)
        )

        return ResponseEntity.ok().body(responseDTO)
    }
}