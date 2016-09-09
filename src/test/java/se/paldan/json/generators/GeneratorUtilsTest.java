package se.paldan.json.generators;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import se.paldan.json.junit.JsonGeneratorRule;

import java.util.Optional;

/**
 * Created by svantepaldan on 09/09/16.
 */
public class GeneratorUtilsTest {
    @Rule
    public JsonGeneratorRule schemaWithExample = new JsonGeneratorRule("string-schema-with-example.json");

    @Rule
    public JsonGeneratorRule schemaWithoutExample = new JsonGeneratorRule("string-schema.json");

    @Rule
    public JsonGeneratorRule schemaWithoutDescription = new JsonGeneratorRule("string-schema-no-description.json");

    @Test
    public void testProperlyGetsExampleFromString() {
        String testString = "This is a description <example>somevalue</example>";
        String testStringTwo = "<example>somevalue</example>This is a description";
        String testStringThree = "This is <example>somevalue</example> a description";
        String testStringFour = "This is <example2>somevalue</example2> a description";

        Assert.assertEquals("somevalue", GeneratorUtils.getExampleValueFromString(testString).get());
        Assert.assertEquals("somevalue", GeneratorUtils.getExampleValueFromString(testStringTwo).get());
        Assert.assertEquals("somevalue", GeneratorUtils.getExampleValueFromString(testStringThree).get());
        Assert.assertFalse(GeneratorUtils.getExampleValueFromString(testStringFour).isPresent());
    }

    @Test
    public void testProperlyGetsExampleFromSchema() {
        Optional<String> maybeExample = GeneratorUtils.getExampleValue(this.schemaWithExample.getSchema());
        Assert.assertTrue(maybeExample.isPresent());
        Assert.assertEquals(maybeExample.get(), "lolcat");
    }

    @Test
    public void testWorksWithDescriptionNoExample() {
        Optional<String> maybeExample = GeneratorUtils.getExampleValue(this.schemaWithoutExample.getSchema());
        Assert.assertFalse(maybeExample.isPresent());
    }

    @Test
    public void testWorksWithoutDescription() {
        Optional<String> maybeExample = GeneratorUtils.getExampleValue(this.schemaWithoutDescription.getSchema());
        Assert.assertFalse(maybeExample.isPresent());
    }
}
