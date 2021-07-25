package com.cv.service2.service.aws.s3

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// https://github.com/deepjavalibrary/djl-spring-boot-starter-demo/tree/master/djl-spring-boot-common/src/main/java/com/aws/samples/djl/spring/common

@Configuration
class AwsS3Configuration {

    @Bean
    fun providesImageUploader(): ImageUploaderService {
        return AwsS3ImageUploaderService()
    }

    @Bean
    fun providesImageDownloader(): ImageDownloaderService {
        return AwsS3ImageDownloaderService()
    }
}