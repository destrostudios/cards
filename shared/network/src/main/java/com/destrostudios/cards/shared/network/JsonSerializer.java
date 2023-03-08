package com.destrostudios.cards.shared.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonSerializer extends Serializer<Object> {

    // Kryo creates a new instance of this serializer every time
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper() {{
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

    @Override
    public void write(Kryo kryo, Output output, Object object) {
        try {
            output.writeString(OBJECT_MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object read(Kryo kryo, Input input, Class<Object> type) {
        try {
            return OBJECT_MAPPER.readValue(input.readString(), type);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
