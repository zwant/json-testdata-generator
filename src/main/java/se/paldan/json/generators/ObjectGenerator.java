package se.paldan.json.generators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;

import java.util.List;
import java.util.Map;

class ObjectGenerator implements JsonGenerator<ObjectSchema> {
    private void addSubSchemas(ObjectNode node, ObjectSchema schema) throws IllegalAccessException, InstantiationException {
        List<String> requiredProps = schema.getRequiredProperties();
        for(Map.Entry<String, Schema> subSchema : schema.getPropertySchemas().entrySet()) {
            if (!shouldIncludeProperty(requiredProps, subSchema.getKey())) {
                continue;
            }
            Class<? extends JsonGenerator> generatorClazz = GeneratorUtils.determineGeneratorType(subSchema.getValue());

            JsonNode subTree = generatorClazz.newInstance().build(subSchema.getValue());
            node.set(subSchema.getKey(), subTree);
        }
    }

    @Override
    public ObjectNode build(ObjectSchema schema) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        ObjectSchema objSchema = schema;

        try {
            addSubSchemas(node, objSchema);
        } catch (IllegalAccessException|InstantiationException e) {
            throw new RuntimeException(e);
        }

        return node;
    }

    private boolean shouldIncludeProperty(List<String> requiredProps, String propertyName) {
        // More likely to get it included, than not
        switch(RootGenerator.getOptions().getOptionalsBehaviour()) {
            case INCLUDE_OPTIONALS:
                return true;
            case EXCLUDE_OPTIONALS:
                return requiredProps.contains(propertyName);
            case RANDOM_OPTIONALS:
                return requiredProps.contains(propertyName) || Math.random() < 0.5;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
