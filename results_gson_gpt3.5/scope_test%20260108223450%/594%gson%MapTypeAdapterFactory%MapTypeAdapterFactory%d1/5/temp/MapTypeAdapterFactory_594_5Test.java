package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonToken;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapTypeAdapterFactory_594_5Test {

    ConstructorConstructor constructorConstructor;
    MapTypeAdapterFactory factory;
    Gson gson;

    @BeforeEach
    public void setUp() {
        constructorConstructor = mock(ConstructorConstructor.class);
        // Mock ObjectConstructor to return a new HashMap instance when called
        ObjectConstructor<Map<String, String>> objectConstructor = () -> new HashMap<>();
        when(constructorConstructor.get(any(TypeToken.class))).thenReturn(objectConstructor);

        factory = new MapTypeAdapterFactory(constructorConstructor, true);
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testCreate_withMapType_returnsTypeAdapter() {
        TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
        @SuppressWarnings("unchecked")
        TypeAdapter<Map<String, String>> adapter = (TypeAdapter<Map<String, String>>) factory.create(gson, typeToken);
        assertNotNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testCreate_withNonMapType_returnsNull() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        TypeAdapter<String> adapter = factory.create(gson, typeToken);
        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_returnsExpectedAdapter() throws Exception {
        // Use reflection to access private getKeyAdapter method
        Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
        getKeyAdapterMethod.setAccessible(true);

        Type keyType = String.class;
        Object keyAdapter = getKeyAdapterMethod.invoke(factory, gson, keyType);
        assertNotNull(keyAdapter);
        assertTrue(keyAdapter instanceof TypeAdapter);

        // test with Integer key type
        keyType = Integer.class;
        Object keyAdapterInt = getKeyAdapterMethod.invoke(factory, gson, keyType);
        assertNotNull(keyAdapterInt);
        assertTrue(keyAdapterInt instanceof TypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void testTypeAdapter_readAndWrite() throws IOException {
        TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
        @SuppressWarnings("unchecked")
        TypeAdapter<Map<String, String>> adapter = (TypeAdapter<Map<String, String>>) factory.create(gson, typeToken);

        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        // Write map to JsonWriter
        java.io.StringWriter stringWriter = new java.io.StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        adapter.write(jsonWriter, map);
        jsonWriter.close();

        String json = stringWriter.toString();
        assertTrue(json.contains("key1"));
        assertTrue(json.contains("value1"));

        // Read map from JsonReader
        JsonReader jsonReader = new JsonReader(new java.io.StringReader(json));
        Map<String, String> readMap = adapter.read(jsonReader);
        assertEquals(map, readMap);
    }
}