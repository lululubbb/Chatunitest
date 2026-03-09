package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_421_3Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullCharacter() {
        char value = 'A';
        String property = "charProperty";

        jsonObject.addProperty(property, value);

        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

        assertEquals(property, propertyCaptor.getValue());
        JsonElement element = elementCaptor.getValue();
        assertTrue(element instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) element;
        assertEquals(value, primitive.getAsCharacter());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullCharacter() {
        String property = "nullCharProperty";

        jsonObject.addProperty(property, (Character) null);

        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

        assertEquals(property, propertyCaptor.getValue());
        JsonElement element = elementCaptor.getValue();
        assertSame(JsonNull.INSTANCE, element);
    }

    @Test
    @Timeout(8000)
    public void testAdd_privateMethodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Use reflection to invoke private add method directly to verify it adds element correctly
        Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
        addMethod.setAccessible(true);

        String property = "reflectedProperty";
        JsonPrimitive value = new JsonPrimitive('Z');

        addMethod.invoke(jsonObject, property, value);

        JsonElement retrieved = jsonObject.get(property);
        assertNotNull(retrieved);
        assertTrue(retrieved instanceof JsonPrimitive);
        assertEquals('Z', ((JsonPrimitive) retrieved).getAsCharacter());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_addCalledOnce() {
        JsonObject spyObject = spy(new JsonObject());
        spyObject.addProperty("prop", 'x');
        verify(spyObject, times(1)).add(anyString(), any(JsonElement.class));
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_nullProperty() {
        // Adding a property with null key should throw NullPointerException or behave consistently
        assertThrows(NullPointerException.class, () -> jsonObject.addProperty(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_emptyProperty() {
        String emptyProperty = "";
        char value = 'b';
        jsonObject.addProperty(emptyProperty, value);

        JsonElement element = jsonObject.get(emptyProperty);
        assertNotNull(element);
        assertTrue(element.isJsonPrimitive());
        assertEquals(value, element.getAsCharacter());
    }
}