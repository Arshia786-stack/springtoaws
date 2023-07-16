package com.awsspring.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;

@Controller
public class FileUploadController {

    // Replace with your AWS access key and secret key
    private static final String AWS_ACCESS_KEY = "YOUR_AWS_ACCESS_KEY";
    private static final String AWS_SECRET_KEY = "YOUR_AWS_SECRET_KEY";
    private static final String BUCKET_NAME = "YOUR_S3_BUCKET_NAME";

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate a unique file name
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            fileName = System.currentTimeMillis() + "-" + fileName;

            // Create AWS credentials
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(AWS_ACCESS_KEY, AWS_SECRET_KEY);

            // Create an S3 client
            S3Client s3Client = S3Client.builder()
                    .region(Region.US_EAST_1) // Replace with your desired region
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();

            // Upload the file to S3 bucket
            s3Client.putObject(PutObjectRequest.builder().bucket(BUCKET_NAME).key(fileName).build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Return success response
            return "success";
        } catch (IOException | S3Exception e) {
            // Handle exception and return error response
            e.printStackTrace();
            return "error";
        }
    }
}