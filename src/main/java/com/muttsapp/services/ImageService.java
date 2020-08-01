package com.muttsapp.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.muttsapp.NewMessageException;
import com.muttsapp.tables.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@Service
public class ImageService {

    @Autowired
    ChatService chatService;

    public void uploadFile(MultipartFile file, int chatId, int userId) throws IOException {
        Regions clientRegion = Regions.US_EAST_2;
        String bucketName = "muttsapp";
        String fileObjKeyName = file.getOriginalFilename();

        try {

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .build();

            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, convert(file))
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.addUserMetadata("title", "someTitle");
            request.setMetadata(metadata);
            s3Client.putObject(request);
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        // save image url to database
        Message m = new Message();
        String url = "https://muttsapp.s3.us-east-2.amazonaws.com/" + file.getOriginalFilename();
        m.setMessage(url);
        m.setChatId(chatId);
        m.setUserId(userId);
        m.setImage(true);
        try {
            chatService.saveMessage(m);
        } catch (NewMessageException e) {
            e.printStackTrace();
        }
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") +
                System.getProperty("file.separator" ) +
                file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

}

