package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.RandomUtils;
import org.everit.json.schema.NumberSchema;

import java.math.BigDecimal;
import java.util.Optional;

class NumberGenerator implements JsonGenerator<NumberSchema> {
    @Override
    public NumericNode build(NumberSchema schema) {
        Optional<String> maybeExample = GeneratorUtils.getExampleValue(schema);
        if(maybeExample.isPresent()) {
            String exampleString = maybeExample.get();
            try {
                return IntNode.valueOf(Integer.parseInt(exampleString));
            } catch (NumberFormatException e) {
                try {
                    return DecimalNode.valueOf(BigDecimal.valueOf(Double.parseDouble(exampleString)));
                } catch (NumberFormatException ex) {
                    throw new RuntimeException("Unable to parse example value " + exampleString
                            + " as either Int or Decimal!");
                }
            }
        }
        Number maybeMin = schema.getMinimum();
        Number maybeMax = schema.getMaximum();
        if(schema.requiresInteger()) {
            int min = getMinValueAsInt(maybeMin, schema.isExclusiveMinimum());
            int max = getMaxValueAsInt(maybeMax, schema.isExclusiveMaximum());
            return IntNode.valueOf(getRandomInt(min, max, schema.getMultipleOf()));
        } else {
            double min = getMinValueAsDouble(maybeMin, schema.isExclusiveMinimum());
            double max = getMaxValueAsDouble(maybeMax, schema.isExclusiveMaximum());
            return DecimalNode.valueOf(BigDecimal.valueOf(getRandomDouble(min, max, schema.getMultipleOf())));
        }
    }

    private int getRandomInt(int min, int max, @Nullable Number shouldBeMultipleOf) {
        if (shouldBeMultipleOf == null) {
            return RandomUtils.nextInt(min, max);
        } else {
            // Note: this could overflow!
            return shouldBeMultipleOf.intValue() * RandomUtils.nextInt(min, max);
        }
    }

    private double getRandomDouble(double min, double max, @Nullable Number shouldBeMultipleOf) {
        if (shouldBeMultipleOf == null) {
            return RandomUtils.nextDouble(min, max);
        } else {
            // Note: this could overflow!
            return shouldBeMultipleOf.doubleValue() * RandomUtils.nextDouble(min, max);
        }
    }

    private int getMinValueAsInt(@Nullable Number min, boolean isExclusive) {
        // TODO: handle negative numbers
        if (min == null) {
            return 0;
        } else if (isExclusive) {
            return min.intValue() + 1;
        }
        return min.intValue();
    }

    private int getMaxValueAsInt(@Nullable Number max, boolean isExclusive) {
        if (max == null) {
            return Integer.MAX_VALUE;
        } else if (isExclusive) {
            return max.intValue() - 1;
        }
        return max.intValue();
    }

    private double getMinValueAsDouble(@Nullable Number min, boolean isExclusive) {
        // TODO: handle negative numbers
        if (min == null) {
            return 0.0;
        } else if (isExclusive) {
            // This is kinda hacky, beware if wanting extreme precision
            return min.doubleValue() + 0.00000001;
        }
        return min.doubleValue();
    }


    private double getMaxValueAsDouble(@Nullable Number max, boolean isExclusive) {
        if (max == null) {
            return Double.MAX_VALUE;
        } else if (isExclusive) {
            // This is kinda hacky, beware if wanting extreme precision
            return max.doubleValue() - 0.00000001;
        }
        return max.doubleValue();
    }
}
