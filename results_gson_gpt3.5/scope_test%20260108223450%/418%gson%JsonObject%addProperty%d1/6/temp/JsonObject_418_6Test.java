package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_418_6Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddPropertyWithNonNullValue() {
        String property = "key";
        String value = "value";

        doNothing().when(jsonObject).add(eq(property), any(JsonElement.class));

        jsonObject.addProperty(property, value);

        verify(jsonObject, times(1)).add(eq(property), argThat(jsonElement ->
                jsonElement instanceof JsonPrimitive &&
                value.equals(((JsonPrimitive) jsonElement).getAsString())
        ));
    }

    @Test
    @Timeout(8000)
    public void testAddPropertyWithNullValue() {
        String property = "key";
        String value = null;

        doNothing().when(jsonObject).add(eq(property), any(JsonElement.class));

        jsonObject.addProperty(property, value);

        verify(jsonObject, times(1)).add(eq(property), eq(JsonNull.INSTANCE));
    }

    @Test
    @Timeout(8000)
    public void testAddPropertyInvokesPrivateAdd() throws Exception {
        String property = "testProperty";
        String value = "testValue";

        JsonObject obj = new JsonObject();

        Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
        addMethod.setAccessible(true);

        // Spy on obj to verify add() call
        JsonObject spyObj = spy(obj);

        spyObj.addProperty(property, value);

        verify(spyObj, times(1)).add(eq(property), argThat(jsonElement ->
                jsonElement instanceof JsonPrimitive &&
                value.equals(((JsonPrimitive) jsonElement).getAsString())
        ));
    }
}