package se.paldan.json.generators;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import se.paldan.json.junit.JsonGeneratorRule;

public class JsonGeneratorRuleTest {
    @Rule
    public JsonGeneratorRule jsonGeneratorIncludeOptionals = new JsonGeneratorRule("example-schema.json",
            GeneratorOptions.of(GeneratorOptions.OptionalsBehaviour.INCLUDE_OPTIONALS));

    @Rule
    public JsonGeneratorRule jsonGeneratorExcludeOptionals = new JsonGeneratorRule("example-schema.json",
            GeneratorOptions.of(GeneratorOptions.OptionalsBehaviour.EXCLUDE_OPTIONALS));

    @Test
    public void testIncludeOptionals() {
        JsonNode jsonObject = jsonGeneratorIncludeOptionals.getJsonNode();
        Assert.assertTrue(jsonObject.has("string_min_only"));
    }

    @Test
    public void testExcludeOptionals() {
        JsonNode jsonObject = jsonGeneratorExcludeOptionals.getJsonNode();
        Assert.assertFalse(jsonObject.has("string_min_only"));
    }

    @Test
    public void testGetSchema() {
        Assert.assertNotNull(jsonGeneratorIncludeOptionals.getSchema());
        Assert.assertNotNull(jsonGeneratorIncludeOptionals.getSchemaAsString());
    }

    @Test
    public void testGetJson() {
        Assert.assertNotNull(jsonGeneratorIncludeOptionals.getJsonNode());
        Assert.assertNotNull(jsonGeneratorIncludeOptionals.getJsonString());
    }
}
