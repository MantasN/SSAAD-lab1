package lab1.config;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.*;

@Configuration
@EnableMongoAuditing
public class MongoConf extends AbstractMongoConfiguration {

    private final String uri;
    private final String db;

    @Autowired
    public MongoConf(@Value("${mongodb.uri}") String uri, @Value("${mongodb.db}") String db) {
        this.uri = uri;
        this.db = db;
    }

    @Override
    protected String getDatabaseName() {
        return db;
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(uri));
    }
}
