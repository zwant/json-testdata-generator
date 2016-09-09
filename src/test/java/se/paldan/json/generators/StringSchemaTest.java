package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.TextNode;
import org.everit.json.schema.StringSchema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class StringSchemaTest {
    StringSchema schemaUnderTest;

    @Before
    public void setUp() throws IOException {
        JSONObject rawSchema;
        try (InputStream inputStream = getClass().getResourceAsStream("/string-schema.json")) {
            rawSchema = new JSONObject(new JSONTokener(inputStream));
        }
        // Root schema will always be an ObjectSchema!
        this.schemaUnderTest = (StringSchema)SchemaLoader.load(rawSchema);
    }
    @Test
    public void properlyGeneratesStringMatchingPattern() {
        TextNode generatedNode = new StringGenerator().build(this.schemaUnderTest);
        String generatedString = generatedNode.asText();
        Assert.assertNotNull(generatedString);

        Assert.assertTrue(Pattern.matches("^[0-9]+", generatedString));
        Assert.assertTrue(generatedString.length() >= 5);
        Assert.assertTrue(generatedString.length() <= 10);
    }
}
