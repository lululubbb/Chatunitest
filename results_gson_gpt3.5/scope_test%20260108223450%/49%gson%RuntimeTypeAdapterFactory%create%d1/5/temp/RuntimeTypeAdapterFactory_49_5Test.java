package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeTypeAdapterFactory_49_5Test {

    private RuntimeTypeAdapterFactory<Object> factory;
    private Gson gson;
    private TypeAdapter<JsonElement> jsonElementAdapter;

    private static class BaseType {}
    private static class SubType1 extends BaseType {
        String field1 = "value1";
    }
    private static class SubType2 extends BaseType {
        int field2 = 2;
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Create factory instance via reflection because constructor is private
        var constructor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
        constructor.setAccessible(true);
        factory = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(BaseType.class, "type", false);

        // Add subtypes via reflection to labelToSubtype and subtypeToLabel maps
        Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
        labelToSubtypeField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factory);
        labelToSubtype.put("sub1", SubType1.class);
        labelToSubtype.put("sub2", SubType2.class);

        Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
        subtypeToLabelField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factory);
        subtypeToLabel.put(SubType1.class, "sub1");
        subtypeToLabel.put(SubType2.class, "sub2");

        // Create a mock Gson
        gson = mock(Gson.class);

        // Mock jsonElementAdapter
        jsonElementAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapter);

        // Mock delegate adapters for subtypes
        TypeAdapter<SubType1> sub1Adapter = mock(TypeAdapter.class);
        TypeAdapter<SubType2> sub2Adapter = mock(TypeAdapter.class);

        // Setup gson.getDelegateAdapter(...) to return appropriate delegate adapters
        when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubType1.class)))).thenReturn(sub1Adapter);
        when(gson.getDelegateAdapter(eq(factory), eq(TypeToken.get(SubType2.class)))).thenReturn(sub2Adapter);

        // Setup sub1Adapter.fromJsonTree(...) and toJsonTree(...)
        when(sub1Adapter.fromJsonTree(any(JsonElement.class))).thenAnswer(invocation -> {
            JsonElement jsonElement = invocation.getArgument(0);
            SubType1 obj = new SubType1();
            return obj;
        });
        when(sub1Adapter.toJsonTree(any(SubType1.class))).thenAnswer(invocation -> {
            SubType1 val = invocation.getArgument(0);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("field1", val.field1);
            return jsonObject;
        });

        // Setup sub2Adapter.fromJsonTree(...) and toJsonTree(...)
        when(sub2Adapter.fromJsonTree(any(JsonElement.class))).thenAnswer(invocation -> {
            JsonElement jsonElement = invocation.getArgument(0);
            SubType2 obj = new SubType2();
            return obj;
        });
        when(sub2Adapter.toJsonTree(any(SubType2.class))).thenAnswer(invocation -> {
            SubType2 val = invocation.getArgument(0);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("field2", val.field2);
            return jsonObject;
        });
    }

    @Test
    @Timeout(8000)
    public void testCreateReturnsNullIfTypeIsNull() {
        TypeAdapter<Object> adapter = factory.create(gson, null);
        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testCreateReturnsNullIfTypeNotHandled() {
        // Provide a TypeToken that is not assignable from baseType and recognizeSubtypes is false
        TypeToken<String> stringType = TypeToken.get(String.class);
        TypeAdapter<String> adapter = factory.create(gson, stringType);
        assertNull(adapter);

        // Enable recognizeSubtypes and test with unrelated type
        factory.recognizeSubtypes();
        adapter = factory.create(gson, stringType);
        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testCreateReturnsAdapterAndReadWrite() throws IOException {
        TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);
        TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);
        assertNotNull(adapter);

        // Prepare JSON with type field for reading: {"type":"sub1","field1":"value1"}
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "sub1");
        jsonObject.addProperty("field1", "value1");

        // Mock jsonElementAdapter.read(...) to return the jsonObject
        when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObject);

        // Test read() method of returned adapter
        JsonReader jsonReader = new JsonReader(new StringReader("{}"));
        BaseType readObj = adapter.read(jsonReader);
        assertNotNull(readObj);
        assertTrue(readObj instanceof SubType1);

        // Prepare to test write() method
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Mock jsonElementAdapter.write(...) to write the JsonObject to StringWriter
        doAnswer(invocation -> {
            JsonWriter out = invocation.getArgument(0);
            JsonObject obj = invocation.getArgument(1);
            out.beginObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                out.name(entry.getKey());
                out.value(entry.getValue().getAsString());
            }
            out.endObject();
            return null;
        }).when(jsonElementAdapter).write(any(JsonWriter.class), any(JsonObject.class));

        // Write an instance of SubType1
        SubType1 sub1Instance = new SubType1();
        adapter.write(jsonWriter, sub1Instance);
        jsonWriter.flush();

        String jsonOutput = stringWriter.toString();
        assertTrue(jsonOutput.contains("\"type\""));
        assertTrue(jsonOutput.contains("\"sub1\""));
        assertTrue(jsonOutput.contains("\"field1\""));
        assertTrue(jsonOutput.contains("value1"));
    }

    @Test
    @Timeout(8000)
    public void testReadThrowsIfTypeFieldMissing() throws IOException {
        TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);
        TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);
        assertNotNull(adapter);

        // JSON object missing 'type' field
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("field1", "value1");

        when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObject);

        JsonReader jsonReader = new JsonReader(new StringReader("{}"));

        JsonParseException exception = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
        assertTrue(exception.getMessage().contains("does not define a field named"));
    }

    @Test
    @Timeout(8000)
    public void testReadThrowsIfSubtypeNotRegistered() throws IOException {
        TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);
        TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);
        assertNotNull(adapter);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "unknownSubtype");
        jsonObject.addProperty("field", "value");

        when(jsonElementAdapter.read(any(JsonReader.class))).thenReturn(jsonObject);

        JsonReader jsonReader = new JsonReader(new StringReader("{}"));

        JsonParseException exception = assertThrows(JsonParseException.class, () -> adapter.read(jsonReader));
        assertTrue(exception.getMessage().contains("did you forget to register a subtype?"));
    }

    @Test
    @Timeout(8000)
    public void testWriteThrowsIfSubtypeNotRegistered() throws IOException {
        TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);
        TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);
        assertNotNull(adapter);

        // Create a class not registered
        class UnregisteredSubtype extends BaseType {}

        UnregisteredSubtype instance = new UnregisteredSubtype();

        JsonWriter jsonWriter = new JsonWriter(new StringWriter());

        JsonParseException exception = assertThrows(JsonParseException.class, () -> adapter.write(jsonWriter, instance));
        assertTrue(exception.getMessage().contains("did you forget to register a subtype?"));
    }

    @Test
    @Timeout(8000)
    public void testWriteThrowsIfSubtypeDefinesTypeFieldAndMaintainTypeFalse() throws IOException {
        TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);
        TypeAdapter<BaseType> adapter = factory.create(gson, baseTypeToken);
        assertNotNull(adapter);

        // Setup a subtype adapter that returns a JsonObject with typeFieldName present
        TypeAdapter<SubType1> sub1Adapter = (TypeAdapter<SubType1>) gson.getDelegateAdapter(factory, TypeToken.get(SubType1.class));
        when(sub1Adapter.toJsonTree(any(SubType1.class))).thenAnswer(invocation -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("type", "sub1");
            obj.addProperty("field1", "value1");
            return obj;
        });

        JsonWriter jsonWriter = new JsonWriter(new StringWriter());

        SubType1 instance = new SubType1();

        JsonParseException exception = assertThrows(JsonParseException.class, () -> adapter.write(jsonWriter, instance));
        assertTrue(exception.getMessage().contains("already defines a field named"));
    }

    @Test
    @Timeout(8000)
    public void testCreateWithMaintainTypeTrue() throws IOException {
        // Create factory with maintainType true
        var constructor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
        constructor.setAccessible(true);
        RuntimeTypeAdapterFactory<Object> factoryMaintain = (RuntimeTypeAdapterFactory<Object>) constructor.newInstance(BaseType.class, "type", true);

        // Setup subtype mappings
        Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
        labelToSubtypeField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Class<?>> labelToSubtype = (Map<String, Class<?>>) labelToSubtypeField.get(factoryMaintain);
        labelToSubtype.put("sub1", SubType1.class);
        labelToSubtype.put("sub2", SubType2.class);

        Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
        subtypeToLabelField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Class<?>, String> subtypeToLabel = (Map<Class<?>, String>) subtypeToLabelField.get(factoryMaintain);
        subtypeToLabel.put(SubType1.class, "sub1");
        subtypeToLabel.put(SubType2.class, "sub2");

        // Mock Gson for factoryMaintain
        Gson gsonMaintain = mock(Gson.class);
        TypeAdapter<JsonElement> jsonElementAdapterMaintain = mock(TypeAdapter.class);
        when(gsonMaintain.getAdapter(JsonElement.class)).thenReturn(jsonElementAdapterMaintain);

        TypeAdapter<SubType1> sub1Adapter = mock(TypeAdapter.class);
        when(gsonMaintain.getDelegateAdapter(eq(factoryMaintain), eq(TypeToken.get(SubType1.class)))).thenReturn(sub1Adapter);

        when(sub1Adapter.fromJsonTree(any(JsonElement.class))).thenAnswer(invocation -> new SubType1());
        when(sub1Adapter.toJsonTree(any(SubType1.class))).thenAnswer(invocation -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("field1", "value1");
            obj.addProperty("type", "sub1");
            return obj;
        });

        TypeToken<BaseType> baseTypeToken = TypeToken.get(BaseType.class);
        TypeAdapter<BaseType> adapter = factoryMaintain.create(gsonMaintain, baseTypeToken);
        assertNotNull(adapter);

        // Prepare JSON with type field for reading
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "sub1");
        jsonObject.addProperty("field1", "value1");

        when(jsonElementAdapterMaintain.read(any(JsonReader.class))).thenReturn(jsonObject);

        BaseType readObj = adapter.read(mock(JsonReader.class));
        assertNotNull(readObj);
        assertTrue(readObj instanceof SubType1);

        // Test write with maintainType true: should write original jsonObject without cloning
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        doAnswer(invocation -> {
            JsonWriter out = invocation.getArgument(0);
            JsonObject obj = invocation.getArgument(1);
            out.beginObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                out.name(entry.getKey());
                out.value(entry.getValue().getAsString());
            }
            out.endObject();
            return null;
        }).when(jsonElementAdapterMaintain).write(any(JsonWriter.class), any(JsonObject.class));

        SubType1 instance = new SubType1();
        adapter.write(jsonWriter, instance);
        jsonWriter.flush();

        String jsonOutput = stringWriter.toString();
        assertTrue(jsonOutput.contains("\"type\""));
        assertTrue(jsonOutput.contains("\"sub1\""));
        assertTrue(jsonOutput.contains("\"field1\""));
        assertTrue(jsonOutput.contains("value1"));
    }
}