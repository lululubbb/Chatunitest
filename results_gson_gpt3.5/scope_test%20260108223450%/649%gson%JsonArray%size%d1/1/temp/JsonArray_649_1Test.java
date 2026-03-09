package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonArray_649_1Test {

    private JsonArray jsonArray;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
    }

    @Test
    @Timeout(8000)
    public void testSize_EmptyArray() {
        assertEquals(0, jsonArray.size());
    }

    @Test
    @Timeout(8000)
    public void testSize_WithElements() throws Exception {
        // Access private field 'elements' via reflection
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);

        @SuppressWarnings("unchecked")
        ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

        // Add concrete JsonElement instances by subclassing and implementing deepCopy()
        elements.add(new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return this;
            }
        });
        elements.add(new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return this;
            }
        });
        elements.add(new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return this;
            }
        });

        assertEquals(3, jsonArray.size());
    }
}