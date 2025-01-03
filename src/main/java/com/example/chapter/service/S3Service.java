package com.example.chapter.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.chapter.exception.UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return null;
            }
            if (!checkExtension(originalFilename)) {
                throw new IllegalArgumentException("jpa, jpeg, png 파일만 업로드 가능 : " + originalFilename);
            }
            byte[] bytes = file.getBytes();
            String fileName = generateFileName(originalFilename);
            String contentType = file.getContentType();
            putS3(bytes, fileName, contentType);
            return generateUnsignedUrl(fileName);
        } catch (IOException e) {
            throw new UploadException("파일 업로드 실패: " + e.getMessage());
        }
    }

    private boolean checkExtension(String originalFilename) {
        List<String> allowedTypes = Arrays.asList("jpg", "jpeg", "png");
        String ext = extractExtension(originalFilename);
        log.info("ext{}", ext);
        return allowedTypes.contains(ext);
    }

    private String extractExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private void putS3(byte[] fileBytes, String fileName, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(fileBytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata));
    }

    private String generateFileName(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String id = UUID.randomUUID().toString();
        return id + "." + extension;
    }

    private String generateUnsignedUrl(String objectKey) {
        String baseUrl = "https://" + bucket + ".s3.amazonaws.com/";
        return baseUrl + objectKey;
    }

}
