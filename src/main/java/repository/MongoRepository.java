package repository;

import com.mongodb.client.*;
import com.mongodb.client.gridfs.*;
import org.bson.Document;

public class MongoRepository {
    private final MongoDatabase database;
    private final MongoCollection<Document> artigosCollection;
    private final GridFSBucket gridFSBucket;

    public MongoRepository(String connectionString, String dbName) {
        MongoClient mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(dbName);
        this.artigosCollection = database.getCollection("artigos");
        this.gridFSBucket = GridFSBuckets.create(database);
    }

    public MongoCollection<Document> getArtigosCollection() {
        return artigosCollection;
    }

    public GridFSBucket getGridFSBucket() {
        return gridFSBucket;
    }
}
