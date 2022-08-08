package br.ufu.standdize;

import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest(properties = {"spring.profiles.active=dev"})
public class DatabaseTests {

    final String collection = "tests";

    final String document = "{\"test\":\"test\"}";

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void canCreateCollection() {
        createCollection();

        dropCollection();
    }

    @Test
    void canCreateAndDeleteDocuments() {
        createCollection();

        mongoTemplate.getCollection(collection).insertOne(org.bson.Document.parse(document));

        Assertions.assertNotNull(mongoTemplate.getCollection(collection).findOneAndDelete(Document.parse(document)));

        dropCollection();
    }

    private void createCollection() {
        if (mongoTemplate.collectionExists(collection)) {
            mongoTemplate.dropCollection(collection);
        }

        mongoTemplate.createCollection(collection);

        assert mongoTemplate.collectionExists(collection);
    }

    private void dropCollection() {
        assert mongoTemplate.collectionExists(collection);

        mongoTemplate.dropCollection(collection);
    }

}
