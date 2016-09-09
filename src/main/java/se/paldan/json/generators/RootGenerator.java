package se.paldan.json.generators;

import com.fasterxml.jackson.databind.JsonNode;
import org.everit.json.schema.Schema;
import se.paldan.json.generators.utils.GeneratorUtils;


public class RootGenerator {
    public static class RootGeneratorOptions {
        public static final int TEST = 1;
    }

    private RootGeneratorOptions options;

    public RootGenerator() {
        this.options = new RootGeneratorOptions();
    }

    public RootGenerator(RootGeneratorOptions options) {
        this.options = options;
    }

    public JsonNode build(Schema schema) {
        Class<? extends JsonGenerator> rootClass = GeneratorUtils.determineGeneratorType(schema);
        try {
            return rootClass.newInstance().build(schema);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
