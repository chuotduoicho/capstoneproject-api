//package com.jovinn.capstoneproject.config;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//
//@Configuration
//public class ThirdApiConfig {
//    @Value("${app.gg-service-account-key-file}")
//    private String ggServiceAccountKeyFilePath;
//
//    private static final String APPLICATION_NAME = "Jovinn";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    @Bean
//    public Drive googleDriveManager() throws GeneralSecurityException, IOException {
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        final GoogleCredentials googleCredentials = ServiceAccountCredentials
//                .fromStream(new ClassPathResource(ggServiceAccountKeyFilePath).getInputStream())
//                .createScoped(Collections.singletonList(DriveScopes.DRIVE));
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(googleCredentials);
//
//        // Construct the drive service object.
//        return new Drive.Builder(httpTransport, JSON_FACTORY, requestInitializer)
//                .setApplicationName(APPLICATION_NAME).build();
//    }
//}
