package se.paldan.json.generators;

import org.everit.json.schema.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class GeneratorUtils {
    private static final Pattern EXAMPLE_CAPTURING_REGEX = Pattern.compile("<example>(.*)</example>");
    static Class<? extends JsonGenerator> determineGeneratorType(Schema schema) {
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

    /**
     * Does some weird things to the description field, looking for "<example>" tags to get an example value
     * to use when generating data. Only used if the option ExamplesBehaviour.USE_EXAMPLES is set.
     * @param schema The schema where we want to grab the description from.
     * @return A String representing the example value to use.
     */
    static Optional<String> getExampleValue(Schema schema) {
        String description = schema.getDescription();
        if (description == null || description.isEmpty()) {
            return Optional.empty();
        } else {
            return getExampleValueFromString(description);
        }
    }

    static Optional<String> getExampleValueFromString(String descriptionString) {
        Matcher matcher = EXAMPLE_CAPTURING_REGEX.matcher(descriptionString);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        } else {
            return Optional.empty();
        }
    }
}
