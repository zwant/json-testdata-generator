package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.io.IOException;
import java.io.InputStream;

public class FuzzingTest {
    private final static int NUM_TEST_RUNS = 100;
    Schema rootSchema;

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Before
    public void setUp() throws IOException {
        JSONObject rawSchema;
        try (InputStream inputStream = getClass().getResourceAsStream("/example-schema.json")) {
            rawSchema = new JSONObject(new JSONTokener(inputStream));
        }
        // Root schema will always be an ObjectSchema!
        this.rootSchema = SchemaLoader.load(rawSchema);
    }
    @Test
    public void testManyTimes() {
        for(int i = 0; i < NUM_TEST_RUNS; i++) {
            BaseJsonNode rootNode = new ObjectGenerator().build((ObjectSchema) this.rootSchema);
            rootSchema.validate(new JSONObject(rootNode.toString()));
        }
    }
}
