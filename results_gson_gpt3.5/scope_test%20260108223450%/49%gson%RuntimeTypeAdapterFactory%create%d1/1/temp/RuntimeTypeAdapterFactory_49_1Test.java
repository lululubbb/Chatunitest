package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory_49_1Test {

    private RuntimeTypeAdapterFactory<Object> factory;
    private Gson gson;

    private static final String TYPE_FIELD_NAME = "type";

    static class Base {}
    static class SubTypeA extends Base {}
    static class SubTypeB extends Base {}

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to create an instance of RuntimeTypeAdapterFactory with baseType=Base.class
        Class<?> clazz = Class.forName("com.google.gson.typeadapters.RuntimeTypeAdapterFactory");
        var ctor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
        ctor.setAccessible(true);
        factory = (RuntimeTypeAdapterFactory<Object>) ctor.newInstance(Base.class, TYPE_FIELD_NAME, false);

        // Add subtypes to the factory's maps using reflection
        Field labelToSubtypeField = clazz.getDeclaredField("labelToSubtype");
        labelToSubtypeField.setAccessible(true);
        Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
        labelToSubtype.put("a", SubTypeA.class);
        labelToSubtype.put("b", SubTypeB.class);

        Field subtypeToLabelField = clazz.getDeclaredField("subtypeToLabel");
        subtypeToLabelField.setAccessible(true);
        Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
        subtypeToLabel.put(SubTypeA.class, "a");
        subtypeToLabel.put(SubTypeB.class, "b");

        // Set recognizeSubtypes to true (test both cases)
        Field recognizeSubtypesField = clazz.getDeclaredField("recognizeSubtypes");
        recognizeSubtypesField.setAccessible(true);
        recognizeSubtypesField.set(factory, true);

        gson = mock(Gson.class);
    }

    @Test
    @Timeout(8000)
    public void create_returnsNull_ifTypeIsNull() {
        TypeAdapter<?> adapter = factory.create(gson, null);
        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void create_returnsNull_ifTypeNotHandled() throws Exception {
        // Create a TypeToken with a class not assignable from baseType
        TypeToken<String> stringType = TypeToken.get(String.class);

        // Set recognizeSubtypes true, baseType is Base.class, String.class is not assignable
        TypeAdapter<?> adapter = factory.create(gson, stringType);
        assertNull(adapter);

        // Now set recognizeSubtypes false and test exact equals condition
        Field recognizeSubtypesField = factory.getClass().getDeclaredField("recognizeSubtypes");
        recognizeSubtypesField.setAccessible(true);
        recognizeSubtypesField.set(factory, false);

        adapter = factory.create(gson, TypeToken.get(SubTypeA.class)); // SubTypeA != Base.class
        assertNull(adapter);

        // Exact match returns non-null
        adapter = factory.create(gson, TypeToken.get(Base.class));
        assertNotNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void create_returnsTypeAdapter_withCorrectReadAndWrite() throws Exception {
        // Prepare mocks for gson.getAdapter(JsonElement.class)
        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        // Prepare delegates for subtypes
        TypeAdapter<SubTypeA> subTypeAAdapter = mock(TypeAdapter.class);
        TypeAdapter<SubTypeB> subTypeBAdapter = mock(TypeAdapter.class);

        when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeA.class)))).thenReturn(subTypeAAdapter);
        when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubTypeB.class)))).thenReturn(subTypeBAdapter);

        // Setup labelToSubtype map (already done in setUp)
        // Setup subtypeToLabel map (already done in setUp)

        // Create the adapter
        TypeAdapter<Object> adapter = factory.create(gson, TypeToken.get(Base.class));
        assertNotNull(adapter);

        // Prepare JsonObject with type field for reading
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TYPE_FIELD_NAME, "a");
        jsonObject.addProperty("field1", "value1");

        JsonElement jsonElement = jsonObject;

        // Mock jsonElementAdapter.read to return jsonElement
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonElementAdapter.read(jsonReader)).thenReturn(jsonElement);

        // Mock labelJsonElement.getAsString() to return "a"
        // This is done by jsonObject.get(typeFieldName).getAsString() - JsonPrimitive returns "a"

        // Mock subTypeAAdapter.fromJsonTree to return a SubTypeA instance
        SubTypeA subTypeAInstance = new SubTypeA();
        when(subTypeAAdapter.fromJsonTree(jsonElement)).thenReturn(subTypeAInstance);

        // Test read method
        Object readResult = adapter.read(jsonReader);
        assertSame(subTypeAInstance, readResult);

        // Test read throws JsonParseException if labelJsonElement is null
        JsonObject jsonObjectNoType = new JsonObject();
        JsonElement jsonElementNoType = jsonObjectNoType;
        when(jsonElementAdapter.read(jsonReader)).thenReturn(jsonElementNoType);

        JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
        assertTrue(ex.getMessage().contains("does not define a field named"));

        // Test read throws JsonParseException if delegate is null
        JsonObject jsonObjectUnknownType = new JsonObject();
        jsonObjectUnknownType.addProperty(TYPE_FIELD_NAME, "unknown");
        JsonElement jsonElementUnknownType = jsonObjectUnknownType;
        when(jsonElementAdapter.read(jsonReader)).thenReturn(jsonElementUnknownType);

        JsonParseException ex2 = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
        assertTrue(ex2.getMessage().contains("did you forget to register a subtype?"));

        // Prepare for write tests
        JsonWriter jsonWriter = mock(JsonWriter.class);

        // For write, get class of value and get label and delegate
        SubTypeA value = new SubTypeA();

        // Mock subtypeToLabel.get to return label "a"
        // Already set up in setUp()

        // Mock subtypeToDelegate.get to return subTypeAAdapter
        // Already set up in create method

        // Mock subTypeAAdapter.toJsonTree to return a JsonObject without typeFieldName
        JsonObject toJsonTreeObject = new JsonObject();
        toJsonTreeObject.addProperty("field1", "value1");
        when(subTypeAAdapter.toJsonTree(value)).thenReturn(toJsonTreeObject);

        // Do nothing on jsonElementAdapter.write
        doNothing().when(jsonElementAdapter).write(any(JsonWriter.class), any(JsonElement.class));

        // Call write method
        adapter.write(jsonWriter, value);

        // Verify jsonElementAdapter.write called with a JsonObject that contains type field "a"
        ArgumentCaptor<JsonElement> jsonElementCaptor = ArgumentCaptor.forClass(JsonElement.class);
        verify(jsonElementAdapter).write(eq(jsonWriter), jsonElementCaptor.capture());
        JsonObject writtenObject = jsonElementCaptor.getValue().getAsJsonObject();
        assertEquals("a", writtenObject.get(TYPE_FIELD_NAME).getAsString());
        assertEquals("value1", writtenObject.get("field1").getAsString());

        // Test write throws JsonParseException if delegate is null (unregistered subtype)
        SubTypeB unregisteredValue = new SubTypeB();
        // Remove SubTypeB adapter from subtypeToDelegate map to simulate missing delegate
        Field subtypeToDelegateField = adapter.getClass().getDeclaredField("subtypeToDelegate");
        subtypeToDelegateField.setAccessible(true);
        Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = (Map<Class<?>, TypeAdapter<?>>) subtypeToDelegateField.get(adapter);
        subtypeToDelegate.remove(SubTypeB.class);

        JsonParseException ex3 = assertThrows(JsonParseException.class, () -> adapter.write(jsonWriter, unregisteredValue));
        assertTrue(ex3.getMessage().contains("did you forget to register a subtype?"));

        // Test write throws JsonParseException if jsonObject has typeFieldName already
        when(subTypeAAdapter.toJsonTree(value)).thenReturn(new JsonObject() {{
            addProperty(TYPE_FIELD_NAME, "somevalue");
        }});

        JsonParseException ex4 = assertThrows(JsonParseException.class, () -> adapter.write(jsonWriter, value));
        assertTrue(ex4.getMessage().contains("already defines a field named"));
    }

    @Test
    @Timeout(8000)
    public void create_withMaintainTypeTrue_writesMaintainingTypeField() throws Exception {
        // Create factory with maintainType = true
        Class<?> clazz = factory.getClass();
        var ctor = clazz.getDeclaredConstructor(Class.class, String.class, boolean.class);
        ctor.setAccessible(true);
        RuntimeTypeAdapterFactory<Object> maintainTypeFactory = (RuntimeTypeAdapterFactory<Object>) ctor.newInstance(Base.class, TYPE_FIELD_NAME, true);

        // Setup maps
        Field labelToSubtypeField = clazz.getDeclaredField("labelToSubtype");
        labelToSubtypeField.setAccessible(true);
        Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(maintainTypeFactory);
        labelToSubtype.put("a", SubTypeA.class);

        Field subtypeToLabelField = clazz.getDeclaredField("subtypeToLabel");
        subtypeToLabelField.setAccessible(true);
        Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(maintainTypeFactory);
        subtypeToLabel.put(SubTypeA.class, "a");

        Field recognizeSubtypesField = clazz.getDeclaredField("recognizeSubtypes");
        recognizeSubtypesField.setAccessible(true);
        recognizeSubtypesField.set(maintainTypeFactory, true);

        Gson gsonMock = mock(Gson.class);
        TypeAdapter<JsonElement> jsonElementAdapter = mock(TypeAdapter.class);
        when(gsonMock.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        TypeAdapter<SubTypeA> subTypeAAdapter = mock(TypeAdapter.class);
        when(gsonMock.getDelegateAdapter(eq(maintainTypeFactory), eq(TypeToken.get(SubTypeA.class)))).thenReturn(subTypeAAdapter);

        TypeAdapter<Object> adapter = maintainTypeFactory.create(gsonMock, TypeToken.get(Base.class));
        assertNotNull(adapter);

        JsonWriter jsonWriter = mock(JsonWriter.class);
        SubTypeA value = new SubTypeA();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TYPE_FIELD_NAME, "a");
        when(subTypeAAdapter.toJsonTree(value)).thenReturn(jsonObject);

        doNothing().when(jsonElementAdapter).write(any(JsonWriter.class), any(JsonElement.class));

        adapter.write(jsonWriter, value);

        // Because maintainType is true, jsonElementAdapter.write should be called with original jsonObject (with type field)
        verify(jsonElementAdapter).write(eq(jsonWriter), eq(jsonObject));
    }
}