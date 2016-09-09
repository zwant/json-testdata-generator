package se.paldan.json.generators;

import com.fasterxml.jackson.databind.JsonNode;
import org.everit.json.schema.Schema;


public final class RootGenerator {
    private static GeneratorOptions options = GeneratorOptions.withDefaults();

    public static void setOptions(GeneratorOptions opts) {
        options = opts;
    }

    public static GeneratorOptions getOptions() {
        return options;
    }

    public static JsonNode build(Schema schema) {
        Class<? extends JsonGenerator> rootClass = GeneratorUtils.determineGeneratorType(schema);
        try {
            return rootClass.newInstance().build(schema);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
