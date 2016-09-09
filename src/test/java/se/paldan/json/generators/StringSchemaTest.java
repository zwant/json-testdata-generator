package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.TextNode;
import org.everit.json.schema.StringSchema;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import se.paldan.json.junit.JsonGeneratorRule;

import java.util.regex.Pattern;

public class StringSchemaTest {
    @Rule
    public JsonGeneratorRule schemaWithoutExample = new JsonGeneratorRule("string-schema.json");

    @Rule
    public JsonGeneratorRule schemaWithExample = new JsonGeneratorRule("string-schema-with-example.json",
                                                                       GeneratorOptions.of(GeneratorOptions.ExamplesBehaviour.USE_EXAMPLES));

    @Test
    public void properlyGeneratesStringMatchingPattern() {
        TextNode generatedNode = new StringGenerator().build((StringSchema)this.schemaWithoutExample.getSchema());
        String generatedString = generatedNode.asText();
        Assert.assertNotNull(generatedString);

        Assert.assertTrue(Pattern.matches("^[0-9]+", generatedString));
        Assert.assertTrue(generatedString.length() >= 5);
        Assert.assertTrue(generatedString.length() <= 10);
    }

    @Test
    public void usesExampleIfPresent() {
        TextNode generatedNode = new StringGenerator().build((StringSchema)this.schemaWithExample.getSchema());
        String generatedString = generatedNode.asText();
        Assert.assertNotNull(generatedString);

        Assert.assertEquals("lolcat", generatedString);
    }
}
