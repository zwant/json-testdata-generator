package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.BooleanNode;
import org.everit.json.schema.BooleanSchema;

import java.util.Random;

public class BooleanGenerator implements JsonGenerator<BooleanSchema> {
    @Override
    public BooleanNode build(BooleanSchema schema) {
        return BooleanNode.valueOf(new Random().nextBoolean());
    }
}
