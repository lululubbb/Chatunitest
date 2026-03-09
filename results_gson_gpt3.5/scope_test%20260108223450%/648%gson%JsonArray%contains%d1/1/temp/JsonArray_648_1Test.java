package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_648_1Test {

    private JsonArray jsonArray;
    private JsonElement element1;
    private JsonElement element2;

    @BeforeEach
    public void setUp() {
        jsonArray = new JsonArray();
        element1 = mock(JsonElement.class);
        element2 = mock(JsonElement.class);
    }

    @Test
    @Timeout(8000)
    public void testContains_ElementPresent() throws Exception {
        setElementsField(jsonArray, new ArrayList<JsonElement>() {{
            add(element1);
            add(element2);
        }});
        assertTrue(jsonArray.contains(element1));
    }

    @Test
    @Timeout(8000)
    public void testContains_ElementNotPresent() throws Exception {
        setElementsField(jsonArray, new ArrayList<JsonElement>() {{
            add(element1);
        }});
        assertFalse(jsonArray.contains(element2));
    }

    @Test
    @Timeout(8000)
    public void testContains_EmptyElements() throws Exception {
        setElementsField(jsonArray, new ArrayList<>());
        assertFalse(jsonArray.contains(element1));
    }

    private void setElementsField(JsonArray jsonArray, ArrayList<JsonElement> elements) throws Exception {
        Field elementsField = JsonArray.class.getDeclaredField("elements");
        elementsField.setAccessible(true);
        elementsField.set(jsonArray, elements);
    }
}