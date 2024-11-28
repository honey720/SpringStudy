package com.example.Spring_Study_Presigned_URL.S3

import com.example.Spring_Study_Presigned_URL.DTO.ResponseDTO
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.GetPresignedUrlRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/presigned")
class BucketPresignedController(
        private val bucketService: BucketService
) {

    @PutMapping
    fun getPresignedUrlForUpload(): ResponseEntity<ResponseDTO> {

        val getPresignedUrlRequestDTO = GetPresignedUrlRequest(
            url = bucketService.getPresigndUrlForUpload("test-file")
        )

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = getPresignedUrlRequestDTO
        )
        return ResponseEntity.ok().body(responseDTO)
    }

    @GetMapping
    fun getPresignedUrlForDownload(): ResponseEntity<ResponseDTO> {

        val getPresignedUrlRequestDTO = GetPresignedUrlRequest(
                url = bucketService.getPresigndUrlForDownload("test-file")
        )

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = getPresignedUrlRequestDTO
        )
        return ResponseEntity.ok().body(responseDTO)
    }
}