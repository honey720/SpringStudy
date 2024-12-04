package com.example.Spring_Study_Presigned_URL.S3.DTO.request

data class S3UploadAbortRequest(
        val key: String,
        val uploadId: String
)