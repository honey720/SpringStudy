//package com.example.Spring_Study_Presigned_URL.S3
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
//import software.amazon.awssdk.regions.Region
//import software.amazon.awssdk.services.s3.presigner.S3Presigner
//
//@Configuration
//class S3Config(
//        @Value("\${spring.cloud.aws.credentials.access-key}")
//        private val accessKey: String,
//        @Value("\${spring.cloud.aws.credentials.secret-key}")
//        private val secretey: String,
//        @Value("\${spring.cloud.aws.region.static}")
//        private val region: String
//) {
//
//    @Bean
//    fun s3Presigner(): S3Presigner {
//        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
//        return S3Presigner.builder()
//                .region(Region.of(region))
//                .credentialsProvider(StaticCredentialsProvider.create(credentials))
//                .build()
//    }
//}