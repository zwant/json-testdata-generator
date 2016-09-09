package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.sun.istack.internal.Nullable;
import org.apache.commons.lang3.RandomUtils;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ArrayGenerator implements JsonGenerator<ArraySchema> {
    @Override
    public ArrayNode build(ArraySchema schema) {
        ArrayNode node = JsonNodeFactory.instance.arrayNode();
        Schema allItemSchema = schema.getAllItemSchema();
        Class<? extends JsonGenerator> generatorClazz = GeneratorUtils.determineGeneratorType(allItemSchema);
        Integer minItems = schema.getMinItems();
        Integer maxItems = schema.getMaxItems();
        try {
            List<BaseJsonNode> thingsToAdd = new ArrayList<>();
            for (int i = 0; i < getNumEntriesToGenerate(minItems, maxItems); i++) {
                thingsToAdd.add(generatorClazz.newInstance().build(allItemSchema));
            }
            // There's a potential bug here, where if min and max are close to eachother, and we
            // happen to get many duplicates - the list could be too short.
            if (schema.needsUniqueItems()) {
                Set<BaseJsonNode> setOfThings = new HashSet<>(thingsToAdd);
                setOfThings.forEach(node::add);
            } else {
                thingsToAdd.forEach(node::add);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    private int getNumEntriesToGenerate(@Nullable Integer minNumberAllowed,
                                        @Nullable Integer maxNumberAllowed) {
        if (minNumberAllowed == null) {
            minNumberAllowed = 0;
        }
        if (maxNumberAllowed == null) {
            maxNumberAllowed = 10;
        }
        return RandomUtils.nextInt(minNumberAllowed,
                                   maxNumberAllowed+1);
    }
}

