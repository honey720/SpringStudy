package com.example.Spring_Study_Presigned_URL.S3.DTO.response

data class S3UploadCompleteResponse(
        val fileName: String,
        val url: String,
        val fileSize: Long
)