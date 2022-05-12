package com.health.healthlakeservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
/**
 * File Upload Service for that kicks of AWS Transcribe
 *
 *  This was adapted from a post "Spring Boot + AWS S3 Upload File"
 *  by Tech Geek
 *  https://www.techgeeknext.com/cloud/aws/amazon-s3-springboot-upload-file-in-s3-bucket
 */
public class FileService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    /**
     * Upload File
     * @param keyName
     * @param file
     * @throws IOException
     */
    public void uploadFile(String keyName, MultipartFile file) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        s3Client.putObject(bucketName, keyName, file.getInputStream(), metadata);
    }

    /**
     * Delete File
     * @param fileName
     */
    public void deleteFile(final String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }

    /**
     * Download File
     * @param keyName
     * @return
     * @throws IOException
     */
    public ByteArrayOutputStream downloadFile(String keyName) throws IOException {

        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));

        InputStream is = s3object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[4096];
        while ((len = is.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }

        return outputStream;

    }

    /**
     * List all the files in the S3 bucket
     * @return
     */
    public List<String> listFiles() {

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(bucketName);

        List<String> keys = new ArrayList<>();

        ObjectListing objects = s3Client.listObjects(listObjectsRequest);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }

            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/"))
                    keys.add(item.getKey());
            }

            objects = s3Client.listNextBatchOfObjects(objects);
        }

        return keys;
    }
}
