package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_489_1Test {

    private Gson gson;
    private TypeToken<String> typeToken;
    private JsonSerializer<String> serializer;
    private JsonDeserializer<String> deserializer;
    private TypeAdapterFactory skipPast;

    @BeforeEach
    public void setUp() {
        gson = mock(Gson.class);
        typeToken = TypeToken.get(String.class);
        serializer = mock(JsonSerializer.class);
        deserializer = mock(JsonDeserializer.class);
        skipPast = mock(TypeAdapterFactory.class);
    }

    @Test
    @Timeout(8000)
    public void testConstructorAndFields() throws Exception {
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, deserializer, gson, typeToken, skipPast, true);

        // Use reflection to verify private final fields
        Field serializerField = TreeTypeAdapter.class.getDeclaredField("serializer");
        serializerField.setAccessible(true);
        assertSame(serializer, serializerField.get(adapter));

        Field deserializerField = TreeTypeAdapter.class.getDeclaredField("deserializer");
        deserializerField.setAccessible(true);
        assertSame(deserializer, deserializerField.get(adapter));

        Field gsonField = TreeTypeAdapter.class.getDeclaredField("gson");
        gsonField.setAccessible(true);
        assertSame(gson, gsonField.get(adapter));

        Field typeTokenField = TreeTypeAdapter.class.getDeclaredField("typeToken");
        typeTokenField.setAccessible(true);
        assertSame(typeToken, typeTokenField.get(adapter));

        Field skipPastField = TreeTypeAdapter.class.getDeclaredField("skipPast");
        skipPastField.setAccessible(true);
        assertSame(skipPast, skipPastField.get(adapter));

        Field nullSafeField = TreeTypeAdapter.class.getDeclaredField("nullSafe");
        nullSafeField.setAccessible(true);
        assertTrue(nullSafeField.getBoolean(adapter));
    }

    @Test
    @Timeout(8000)
    public void testRead_withDeserializer() throws Exception {
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, deserializer, gson, typeToken, skipPast, false);

        JsonReader in = mock(JsonReader.class);
        JsonElement element = mock(JsonElement.class);

        when(in.peek()).thenReturn(com.google.gson.stream.JsonToken.STRING);
        when(gson.fromJson(in, JsonElement.class)).thenReturn(element);
        when(deserializer.deserialize(eq(element), eq(typeToken.getType()), any(JsonDeserializationContext.class))).thenReturn("deserialized");

        String result = adapter.read(in);

        assertEquals("deserialized", result);
        verify(deserializer).deserialize(eq(element), eq(typeToken.getType()), any(JsonDeserializationContext.class));
    }

    @Test
    @Timeout(8000)
    public void testRead_noDeserializer_delegate() throws Exception {
        TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

        // Inject delegate via reflection
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(adapter, delegateAdapter);

        JsonReader in = mock(JsonReader.class);
        when(delegateAdapter.read(in)).thenReturn("delegateRead");

        String result = adapter.read(in);

        assertEquals("delegateRead", result);
        verify(delegateAdapter).read(in);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withSerializer() throws Exception {
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(serializer, null, gson, typeToken, skipPast, false);

        JsonWriter out = mock(JsonWriter.class);

        when(serializer.serialize(eq("value"), eq(typeToken.getType()), any(JsonSerializationContext.class))).thenReturn(new JsonPrimitive("serialized"));
        doAnswer(invocation -> {
            // simulate gson.toJson(JsonElement, JsonWriter) writing to out
            return null;
        }).when(gson).toJson(any(JsonElement.class), eq(out));

        adapter.write(out, "value");

        verify(serializer).serialize(eq("value"), eq(typeToken.getType()), any(JsonSerializationContext.class));
        verify(gson).toJson(any(JsonElement.class), eq(out));
    }

    @Test
    @Timeout(8000)
    public void testWrite_noSerializer_delegate() throws Exception {
        TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(adapter, delegateAdapter);

        JsonWriter out = mock(JsonWriter.class);

        adapter.write(out, "value");

        verify(delegateAdapter).write(out, "value");
    }

    @Test
    @Timeout(8000)
    public void testDelegate_lazyInitialization() throws Exception {
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

        // Initially delegate is null
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        assertNull(delegateField.get(adapter));

        // Mock skipPast to return a delegate adapter
        TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
        when(skipPast.create(eq(gson), eq(typeToken))).thenReturn(delegateAdapter);

        // Inject skipPast mock
        Field skipPastField = TreeTypeAdapter.class.getDeclaredField("skipPast");
        skipPastField.setAccessible(true);
        skipPastField.set(adapter, skipPast);

        // Call private delegate() method via reflection
        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);
        TypeAdapter<String> result = (TypeAdapter<String>) delegateMethod.invoke(adapter);

        assertSame(delegateAdapter, result);
        assertSame(delegateAdapter, delegateField.get(adapter));
    }

    @Test
    @Timeout(8000)
    public void testGetSerializationDelegate_returnsDelegate() {
        TreeTypeAdapter<String> adapter = new TreeTypeAdapter<>(null, null, gson, typeToken, skipPast, false);

        TypeAdapter<String> delegateAdapter = mock(TypeAdapter.class);
        try {
            Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
            delegateField.setAccessible(true);
            delegateField.set(adapter, delegateAdapter);
        } catch (Exception e) {
            fail(e);
        }

        TypeAdapter<String> result = adapter.getSerializationDelegate();

        assertSame(delegateAdapter, result);
    }

    @Test
    @Timeout(8000)
    public void testNewFactory_exactType() {
        TypeToken<String> exactType = TypeToken.get(String.class);
        Object typeAdapter = new Object();

        TypeAdapterFactory factory = TreeTypeAdapter.newFactory(exactType, typeAdapter);

        assertNotNull(factory);
    }

    @Test
    @Timeout(8000)
    public void testNewFactoryWithMatchRawType() {
        TypeToken<String> exactType = TypeToken.get(String.class);
        Object typeAdapter = new Object();

        TypeAdapterFactory factory = TreeTypeAdapter.newFactoryWithMatchRawType(exactType, typeAdapter);

        assertNotNull(factory);
    }

    @Test
    @Timeout(8000)
    public void testNewTypeHierarchyFactory() {
        Class<String> hierarchyType = String.class;
        Object typeAdapter = new Object();

        TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, typeAdapter);

        assertNotNull(factory);
    }
}