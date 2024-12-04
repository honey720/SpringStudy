package com.example.Spring_Study_Presigned_URL.S3.DTO.request

data class S3UploadCompleteRequest(
        val key: String, // 임시 저장. 이미지 경로는 도메인에서 가져올 예정
        val uploadId: String,
        val parts: List<Part>,
) {
    data class Part(
            val partNumber: Int,
            val eTag: String,
    )
}