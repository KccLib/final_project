package com.kcc.trioffice.global.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.kcc.trioffice.global.enums.FileType;
import com.kcc.trioffice.global.exception.type.ServerException;
import com.kcc.trioffice.global.image.S3SaveDir;
import com.kcc.trioffice.global.image.dto.response.S3UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class S3FileService {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3UploadFile upload(MultipartFile uploadFile, String s3UploadFilePath, S3SaveDir s3SaveDir) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());

        String fileName = uploadFile.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        long size = uploadFile.getSize();

        String contentType = uploadFile.getContentType();
        FileType fileType;
        // 이미지 파일인지 확인
        if (contentType != null && (contentType.startsWith("image/jpeg") || contentType.startsWith("image/png") || contentType.startsWith("image/gif") || contentType.startsWith("image/svg"))) {
            fileType = FileType.IMAGE;
        } else {
            fileType = FileType.FILE;
        }

        String bucketName = bucket + s3SaveDir.path;
        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, s3UploadFilePath,inputStream,metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            throw new ServerException(e.getMessage());
        }

        String fileUrl = amazonS3Client.getUrl(bucketName, s3UploadFilePath).toString();

        return new S3UploadFile(fileName, fileUrl, fileExtension, size, fileType.getValue(), null);
    }

    public ResponseEntity<byte[]> download(String fileUrl, String fileName) {
        try {
            URI uri = new URI(fileUrl);
            String key = uri.getPath().substring(uri.getPath().indexOf(bucket) + bucket.length() + 1);
            S3Object o = amazonS3Client.getObject(new GetObjectRequest(bucket, key));
            S3ObjectInputStream objectInputStream = ((S3Object) o).getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            String originFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attachment", originFileName);
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    public void delete(String s3DeleteFilePath, S3SaveDir s3SaveDir) {
        String bucketName = bucket + s3SaveDir.path;

        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, s3DeleteFilePath));
        } catch (AmazonServiceException e) {
            throw new ServerException("S3 파일 삭제 실패");
        }
    }

    public ResponseEntity<byte[]> getFileAsResponseEntity(String fileUrl, String fileName, MediaType mediaType) {
        try {
            URI uri = new URI(fileUrl);
            String key = uri.getPath().substring(uri.getPath().indexOf(bucket) + bucket.length() + 1);
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, key));
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(mediaType);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("inline", URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
