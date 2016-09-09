package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.TextNode;
import com.mifmif.common.regex.Generex;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.everit.json.schema.StringSchema;

import java.util.regex.Pattern;

public class StringGenerator implements JsonGenerator<StringSchema> {
    private final static int PREFERRED_STRING_LENGTH = 50;

    @Override
    public TextNode build(StringSchema schema) {
        int min = getMinLength(schema.getMinLength());
        int max = getMaxLength(min, schema.getMaxLength());
        Pattern regexPattern = schema.getPattern();

        if (regexPattern != null) {
            return TextNode.valueOf(getStringMatchingRegex(regexPattern, min, max));
        } else {
            return TextNode.valueOf(getRandomAlphabetic(min, max));
        }
    }

    private static int getMinLength(@Nullable Integer maybeMin) {
        if (maybeMin == null) {
            return 0;
        } else {
            return maybeMin;
        }
    }

    private static int getMaxLength(int min, @Nullable Integer maybeMax) {
        if (maybeMax == null) {
            return min + PREFERRED_STRING_LENGTH;
        } else {
            return maybeMax;
        }
    }

    private static String getAutomatonFriendlyRegex(Pattern pattern) {
        String patternString = pattern.toString();
        // ^ (beginning of line) and $ (end of line) aren't supported in Generex/Automaton, strip them
        if (patternString.startsWith("^")) {
            patternString = patternString.substring(1);
        }
        if (patternString.endsWith("$")) {
            patternString = patternString.substring(0, patternString.length()-1);
        }
        return patternString;
    }

    private static String getStringMatchingRegex(Pattern pattern, int min, int max) {
        String stringPattern = getAutomatonFriendlyRegex(pattern);
        Generex generex = new Generex(stringPattern);
        return generex.random(min, max);
    }

    private static String getRandomAlphabetic(int minLength, int maxLength) {
        return RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(minLength, maxLength + 1));
    }
}
