package se.paldan.json.generators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import se.paldan.json.junit.JsonGeneratorRule;

import java.io.IOException;
import java.util.List;

public class FuzzingTest {
    static class ExampleSchemaObject {
        static class NestedObject {
            public String required_string;
            public String optional_string;
        }
        public String string_min_only;
        public String string_max_only;
        public String string_min_and_max;
        public String string_with_simple_pattern;
        public String string_with_simple_pattern_and_min;
        public String string_with_simple_pattern_and_max;
        public String string_with_simple_pattern_and_min_and_max;
        public String string_with_more_complex_pattern;
        public int int_min_only;
        public int int_max_only;
        public int int_min_and_max;
        public List<String> array_min_and_unique;
        public NestedObject nested_object;
    }
    private final static int NUM_TEST_RUNS = 100;

    @Rule
    public JsonGeneratorRule jsonGenerator = new JsonGeneratorRule("example-schema.json");

    @Test
    public void testManyTimes() {
        for (int i = 0; i < NUM_TEST_RUNS; i++) {
            jsonGenerator.getSchema().validate(new JSONObject(jsonGenerator.getJsonString()));
        }
    }

    @Test
    public void testCanValidateContents() {
        JsonNode jsonObject = jsonGenerator.getJsonNode();
        // string_min_only is optional! So it might be missing for very good reasons :)
        if(!jsonGenerator.getJsonNode().has("string_min_only")) {
            Assert.assertFalse(jsonObject.has("string_min_only"));
        } else {
            Assert.assertEquals(jsonGenerator.getJsonNode().get("string_min_only").asText(),
                                jsonObject.get("string_min_only").asText());
        }
    }

    @Test
    public void testCanParseAndValidateContents() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ExampleSchemaObject exampleSchemaObject = mapper.readValue(jsonGenerator.getJsonString(),
                                                                   ExampleSchemaObject.class);
        // string_min_only is optional! So it might be missing for very good reasons :)
        if(!jsonGenerator.getJsonNode().has("string_min_only")) {
            Assert.assertNull(exampleSchemaObject.string_min_only);
        } else {
            Assert.assertEquals(jsonGenerator.getJsonNode().get("string_min_only").asText(),
                    exampleSchemaObject.string_min_only);
        }

        // nested_object.required_string is required, so we should not fail on this - ever!
        JsonNode nestedObjectNode = jsonGenerator.getJsonNode().get("nested_object");
        Assert.assertNotNull(nestedObjectNode);
        Assert.assertNotNull(nestedObjectNode.get("required_string"));
        Assert.assertEquals(nestedObjectNode.get("required_string").asText(),
                            exampleSchemaObject.nested_object.required_string);
    }
}
