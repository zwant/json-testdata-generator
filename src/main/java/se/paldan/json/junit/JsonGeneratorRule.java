package se.paldan.json.junit;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Resources;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.rules.ExternalResource;
import se.paldan.json.generators.ObjectGenerator;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

public class JsonGeneratorRule extends ExternalResource {
    private final ObjectSchema schema;

    private JsonNode jsonNode;
    private String jsonString;

    /**
     * Construct a rule which reads the schema from <code>schemaFilePathFromResources</code>
     * in resources relative to the classloader of <code>testClassInstance</code>.
     * @param testClassInstance The class which we will use for getting the classloader for reading resources
     * @param schemaFilePathFromResources The path (resource identifier) to where we want to load the json schema
     *                                    definition. Note: Needs a '/' prefix!
     */
    public JsonGeneratorRule(Object testClassInstance, String schemaFilePathFromResources) {
        this(Resources.getResource(testClassInstance.getClass(), schemaFilePathFromResources));
    }

    /**
     * Construct a rule which reads the schema from <code>schemaFilePathFromResources</code>
     * in resources from the context class path.
     * @param schemaFilePathFromResources The path (resource identifier) to where we want to load the json schema
     *                                    definition. Note: Without a '/' prefix!
     */
    public JsonGeneratorRule(String schemaFilePathFromResources) {
        this(Resources.getResource(schemaFilePathFromResources));
    }

    /**
     * Construct a rule that reads it's schema from the provided URL (local filesystem only!)
     * @param schemaFileUrl The URL, on the local filesystem, of the schema to load.
     */
    private JsonGeneratorRule(URL schemaFileUrl) {
        String schemaFileAsString;
        try {
            schemaFileAsString = Resources.toString(schemaFileUrl, Charset.defaultCharset());
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject schemaObject = new JSONObject(new JSONTokener(schemaFileAsString));

        this.schema = (ObjectSchema) SchemaLoader.load(schemaObject);
    }

    @Override
    protected void before() throws Throwable {
        this.jsonNode = new ObjectGenerator().build(this.schema);
        this.jsonString = this.jsonNode.toString();
    }

    public ObjectSchema getSchema() {
        return schema;
    }

    public JsonNode getJsonNode() {
        return jsonNode;
    }

    public String getJsonString() {
        return jsonString;
    }
}
