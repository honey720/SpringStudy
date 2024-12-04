package com.example.Spring_Study_Presigned_URL.S3.DTO.response

data class S3UploadInitiateResponse(
        val uploadId: String,
        val key: String
)