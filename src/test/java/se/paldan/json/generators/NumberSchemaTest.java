package se.paldan.json.generators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.google.common.io.Resources;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import se.paldan.json.junit.JsonGeneratorRule;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class NumberSchemaTest {
    @Rule
    public JsonGeneratorRule integerSchemaUnderTest = new JsonGeneratorRule("integer-schema.json");

    @Rule
    public JsonGeneratorRule doubleSchemaUnderTest = new JsonGeneratorRule("double-schema.json");

    @Rule
    public JsonGeneratorRule integerSchemaUnderTestWithExamples =
            new JsonGeneratorRule("double-schema-with-example.json",
                    GeneratorOptions.of(GeneratorOptions.ExamplesBehaviour.USE_EXAMPLES));


    @Rule
    public JsonGeneratorRule doubleSchemaUnderTestWithExamples =
            new JsonGeneratorRule("double-schema-with-example.json",
                    GeneratorOptions.of(GeneratorOptions.ExamplesBehaviour.USE_EXAMPLES));

    @Test
    public void properlyGeneratesIntegers() {
        NumericNode generatedNode = new NumberGenerator().build((NumberSchema)this.integerSchemaUnderTest.getSchema());
        int generatedInt = generatedNode.asInt();
        Assert.assertTrue(generatedInt >= 5);
        Assert.assertTrue(generatedInt <= 10);
    }

    @Test
    public void properlyGeneratesDoubles() {
        NumericNode generatedNode =
                new NumberGenerator().build((NumberSchema)this.doubleSchemaUnderTest.getSchema());
        double generatedDouble = generatedNode.asDouble();
        Assert.assertTrue(generatedDouble >= 5);
        Assert.assertTrue(generatedDouble <= 10);
    }

    @Test
    public void properlyGeneratesIntegersWithExamples() {
        NumericNode generatedNode =
                new NumberGenerator().build((NumberSchema)this.integerSchemaUnderTestWithExamples.getSchema());
        int generatedInt = generatedNode.asInt();
        Assert.assertEquals(5, generatedInt);
    }

    @Test
    public void doesNotGenerateIntegersWithInvalidExample() throws IOException {
        URL schemaFile = Resources.getResource("integer-schema-with-invalid-example.json");
        String schemaData = Resources.toString(schemaFile, Charset.defaultCharset());
        JSONObject schemaObject = new JSONObject(new JSONTokener(schemaData));
        Schema schema = SchemaLoader.load(schemaObject);
        RootGenerator.setOptions(GeneratorOptions.of(GeneratorOptions.ExamplesBehaviour.USE_EXAMPLES));
        try {
            JsonNode generatedNode =
                    RootGenerator.build(schema);
        } catch(RuntimeException e) {
            Assert.assertTrue(true);
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void properlyGeneratesDoublesWithExamples() {
        NumericNode generatedNode =
                new NumberGenerator().build((NumberSchema)this.doubleSchemaUnderTestWithExamples.getSchema());
        double generatedDouble = generatedNode.asDouble();
        Assert.assertEquals(5.456789, generatedDouble, 0.00001);
    }
}
