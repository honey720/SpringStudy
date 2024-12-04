package com.example.Spring_Study_Presigned_URL.S3.DTO.request

data class S3UploadInitiateRequest(
        val originalFileName: String,
        val fileType: String,
        val fileSize: Long,
)