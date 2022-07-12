package br.ufu.standdize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class DatabaseConnectionTests {

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void setUp() {
        final String collection = "tests";

        mongoTemplate.createCollection(collection);

        assert mongoTemplate.collectionExists(collection);
    }

}
