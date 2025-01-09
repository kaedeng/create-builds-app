package com.create_builds.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3;
    private final String bucketName = "cba-s3-bucket";

    public S3Service() {
        String accessKeyId = System.getenv("AMAZON_ID");
        String secretAccessKey = System.getenv("AMAZON_SECRET_ID");

        if (accessKeyId == null || secretAccessKey == null) {
            throw new IllegalStateException("AWS credentials are not set in the environment variables.");
        }

        this.s3 = S3Client.builder()
                .region(Region.US_WEST_2) // Hardcoded region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }

    public String uploadFile(MultipartFile file, String keyPrefix, String fileName) {
        try {

            Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            String s3Key = keyPrefix + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            s3.putObject(putObjectRequest, tempFile);

            Files.delete(tempFile);

            return "https://" + bucketName + ".s3." + Region.US_WEST_2.id() + ".amazonaws.com/" + s3Key;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public void deleteFilesInFolder(String folderKey) {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderKey)
                .build();

        ListObjectsV2Response listObjectsResponse = s3.listObjectsV2(listObjectsRequest);
        List<S3Object> objects = listObjectsResponse.contents();

        for (S3Object object : objects) {
            String key = object.key();
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        }
    }
}
