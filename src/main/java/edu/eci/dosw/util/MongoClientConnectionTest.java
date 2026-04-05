package edu.eci.dosw.util;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Clase de prueba para validar la conexión a MongoDB Atlas.
 * Ejecutar como: java -cp ... MongoClientConnectionTest
 */
public class MongoClientConnectionTest {
    public static void main(String[] args) {
        // Reemplaza <db_password> con tu contraseña real
        String connectionString = "mongodb+srv://juandavidvaleroa_db_user:<Gaby123>@cluster0.yjwhg73.mongodb.net/?appName=Cluster0";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("✅ Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                System.err.println("❌ Error connecting to MongoDB:");
                e.printStackTrace();
            }
        }
    }
}
