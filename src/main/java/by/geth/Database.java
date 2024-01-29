package by.geth;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;

import static com.google.firebase.FirebaseOptions.builder;

public class Database {
    public Database(ServerLocation location) {
        try {
            if (location == ServerLocation.USA) {
                InputStream credStream = Main.class.getClassLoader().getResourceAsStream("operators.json");
                if (credStream != null) {
                    FirebaseOptions options = builder()
                            .setCredentials(GoogleCredentials.fromStream(credStream))
                            .setDatabaseUrl("https://operators-5f1b2.firebaseio.com")
                            .build();
                    FirebaseApp.initializeApp(options);
                } else {
                    System.err.println("Failed to initialize the realtime database!");
                }
            } else if (location == ServerLocation.EU) {
                InputStream credStream = Main.class.getClassLoader().getResourceAsStream("intercom-eu.json");
                if (credStream != null) {
                    FirebaseOptions options = builder()
                            .setCredentials(GoogleCredentials.fromStream(credStream))
                            .setDatabaseUrl("https://intercom-eu-default-rtdb.europe-west1.firebasedatabase.app")
                            .build();
                    FirebaseApp.initializeApp(options);
                } else {
                    System.err.println("Failed to initialize the realtime database!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeValue(String path, Object value) {
        FirebaseDatabase.getInstance().getReference(path).setValueAsync(value);
    }
}
