package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.NumericNode;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class NumberSchemaTest {
    NumberSchema integerSchemaUnderTest;
    NumberSchema doubleSchemaUnderTest;

    @Before
    public void setUp() throws IOException {
        JSONObject rawSchema;
        try (InputStream inputStream = getClass().getResourceAsStream("/integer-schema.json")) {
            rawSchema = new JSONObject(new JSONTokener(inputStream));
        }
        // Root schema will always be an ObjectSchema!
        this.integerSchemaUnderTest = (NumberSchema)SchemaLoader.load(rawSchema);

        try (InputStream inputStream = getClass().getResourceAsStream("/double-schema.json")) {
            rawSchema = new JSONObject(new JSONTokener(inputStream));
        }
        // Root schema will always be an ObjectSchema!
        this.doubleSchemaUnderTest = (NumberSchema)SchemaLoader.load(rawSchema);
    }
    @Test
    public void properlyGeneratesIntegers() {
        NumericNode generatedNode = new NumberGenerator().build(this.integerSchemaUnderTest);
        int generatedInt = generatedNode.asInt();
        Assert.assertTrue(generatedInt >= 5);
        Assert.assertTrue(generatedInt <= 10);
    }

    @Test
    public void properlyGeneratesDoubles() {
        NumericNode generatedNode = new NumberGenerator().build(this.doubleSchemaUnderTest);
        double generatedDouble = generatedNode.asDouble();
        Assert.assertTrue(generatedDouble >= 5);
        Assert.assertTrue(generatedDouble <= 10);
    }
}
