package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class JsonObject_421_2Test {

    private JsonObject jsonObject;

    @BeforeEach
    public void setUp() {
        jsonObject = spy(new JsonObject());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNonNullCharacter() throws Exception {
        // Arrange
        String property = "charProp";
        Character value = 'a';

        // Act
        jsonObject.addProperty(property, value);

        // Assert
        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

        assertEquals(property, propertyCaptor.getValue());
        JsonElement element = elementCaptor.getValue();
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(value.charValue(), ((JsonPrimitive) element).getAsCharacter());
    }

    @Test
    @Timeout(8000)
    public void testAddProperty_withNullCharacter() throws Exception {
        // Arrange
        String property = "nullCharProp";
        Character value = null;

        // Act
        jsonObject.addProperty(property, value);

        // Assert
        ArgumentCaptor<String> propertyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JsonElement> elementCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonObject).add(propertyCaptor.capture(), elementCaptor.capture());

        assertEquals(property, propertyCaptor.getValue());
        JsonElement element = elementCaptor.getValue();
        // JsonNull.INSTANCE is singleton, so check equality
        assertSame(JsonNull.INSTANCE, element);
    }

    @Test
    @Timeout(8000)
    public void testAdd_privateMethod_addInvokedCorrectly() throws Exception {
        // Use reflection to get private field 'members'
        Field membersField = JsonObject.class.getDeclaredField("members");
        membersField.setAccessible(true);
        LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap<>(false);
        membersField.set(jsonObject, members);

        // Use reflection to invoke private add method
        Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
        addMethod.setAccessible(true);

        String property = "key";
        JsonPrimitive value = new JsonPrimitive('z');

        addMethod.invoke(jsonObject, property, value);

        // Validate that members map contains the property and value
        assertTrue(members.containsKey(property));
        assertEquals(value, members.get(property));
    }
}