package com.example.Spring_Study_Presigned_URL.DTO

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseDTO(
        @JsonProperty("success") val isSuccess: Boolean,
        @JsonProperty("state") val stateCode: Int,
        @JsonProperty("result") val result: Any
)
