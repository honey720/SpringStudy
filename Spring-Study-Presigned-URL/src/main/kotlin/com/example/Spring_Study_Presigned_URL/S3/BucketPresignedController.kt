package com.example.Spring_Study_Presigned_URL.S3

import com.example.Spring_Study_Presigned_URL.DTO.ResponseDTO
import com.example.Spring_Study_Presigned_URL.S3.DTO.request.GetPresignedUrlRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/presigned")
class BucketPresignedController(
        private val bucketService: BucketService
) {

    @PutMapping
    fun getPresignedUrlForUpload(): ResponseEntity<ResponseDTO> {

        val currentTimestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val objectName = "$currentTimestamp.jpg"

        val getPresignedUrlRequestDTO = GetPresignedUrlRequest(
                url = bucketService.getPresigndUrlForUpload(objectName),
                objectName = objectName
        )

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = getPresignedUrlRequestDTO
        )
        return ResponseEntity.ok().body(responseDTO)
    }

    @GetMapping("/{objectName}")
    fun getPresignedUrlForDownload(@PathVariable objectName: String): ResponseEntity<ResponseDTO> {

        val getPresignedUrlRequestDTO = GetPresignedUrlRequest(
                url = bucketService.getPresigndUrlForDownload(objectName),
                objectName = objectName
        )

        val responseDTO = ResponseDTO(
                isSuccess = true,
                stateCode = 200,
                result = getPresignedUrlRequestDTO
        )
        return ResponseEntity.ok().body(responseDTO)
    }
}