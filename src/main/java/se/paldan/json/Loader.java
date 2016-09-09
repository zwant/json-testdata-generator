package se.paldan.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import se.paldan.json.generators.ObjectGenerator;

import java.io.IOException;
import java.io.InputStream;

public class Loader {
    public static void main(String[] args) throws IOException {
        try (InputStream inputStream = Loader.class.getResourceAsStream("/example-schema.json")) {
            JsonNode rootNode = getJsonFromSchemaFile(inputStream);
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
        }
    }

    public static JsonNode getJsonFromSchema(JSONObject schemaObject) {
        // Root schema will always be an ObjectSchema!
        ObjectSchema rootSchema = (ObjectSchema) SchemaLoader.load(schemaObject);

        return new ObjectGenerator().build(rootSchema);
    }

    public static JsonNode getJsonFromSchemaFile(InputStream inputStream) {
        JSONObject schemaObject = new JSONObject(new JSONTokener(inputStream));
        return getJsonFromSchema(schemaObject);
    }
}
