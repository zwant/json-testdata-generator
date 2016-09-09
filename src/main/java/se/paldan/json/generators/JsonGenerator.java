package se.paldan.json.generators;

import com.fasterxml.jackson.databind.node.BaseJsonNode;
import org.everit.json.schema.Schema;

public interface JsonGenerator<T extends Schema> {
    BaseJsonNode build(T schema);
}
