package se.paldan.json.generators.utils;

import org.everit.json.schema.*;
import se.paldan.json.generators.*;

public class GeneratorUtils {
    public static Class<? extends JsonGenerator> determineGeneratorType(Schema schema) {
        if (schema instanceof ObjectSchema) {
            return ObjectGenerator.class;
        } else if (schema instanceof StringSchema) {
            return StringGenerator.class;
        } else if (schema instanceof NumberSchema) {
            return NumberGenerator.class;
        } else if (schema instanceof ArraySchema) {
            return ArrayGenerator.class;
        }
        throw new IllegalArgumentException("Are you kidding me?! I cant build " + schema.getClass());
    }
}
