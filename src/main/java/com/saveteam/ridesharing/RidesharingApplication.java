package com.saveteam.ridesharing;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.*;

@SpringBootApplication
@EnableAsync
public class RidesharingApplication extends SpringBootServletInitializer {
    public static final String SERVICE_ACCOUNT = "xxx";

    public static final String DatabaseUrl = "https://xxx.firebaseio.com/";

    public static void main(String[] args) {
        SpringApplication.run(RidesharingApplication.class, args);
        try {
            InputStream serviceAccount = new ByteArrayInputStream(SERVICE_ACCOUNT.getBytes());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(DatabaseUrl)
                    .build();
            FirebaseApp.initializeApp(options);

            // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("testpaths");
            ref.setValueAsync(null);

            DataManager dataManager = DataManager.getInstance();
            // dataManager.updateData();
            System.out.println("update");
        }catch (IOException e) {
            System.out.println("error");
        }

        ParamManager paramManager = ParamManager.getInstance();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RidesharingApplication .class);
    }

}
