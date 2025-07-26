package io.github.ericksjp.s3demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.ericksjp.s3demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import io.github.ericksjp.s3demo.models.InputStreamObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class S3Service {
    private final AmazonS3 s3Client;
    private final String s3Bucket;

    public void upload(
            final String keyName,
            final String contentType,
            final InputStreamObject value
    ) throws AmazonClientException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(value.size());
        metadata.setContentType(contentType);

        s3Client.putObject(s3Bucket, keyName, value.stream(), metadata);
    }

    public byte[] download(final String keyName) throws IOException, AmazonClientException {
        S3Object s3Object;
        try {
            s3Object = s3Client.getObject(s3Bucket, keyName);
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                throw new ResourceNotFoundException(String.format("'%s' not found", keyName));
            }

            throw e;
        }

        InputStream inputStream = s3Object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        int len = inputStream.read(buffer, 0, buffer.length);
        while (len != -1) {
            outputStream.write(buffer, 0, len);
            len = inputStream.read(buffer, 0, buffer.length);
        }

        inputStream.close();
        outputStream.close();

        return outputStream.toByteArray();
    }
}
