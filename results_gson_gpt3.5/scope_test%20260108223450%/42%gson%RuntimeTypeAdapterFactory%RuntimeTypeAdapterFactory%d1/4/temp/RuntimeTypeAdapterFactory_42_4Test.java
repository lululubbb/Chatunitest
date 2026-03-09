package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonPrimitive;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RuntimeTypeAdapterFactory_42_4Test {

    static class Base {}
    static class SubTypeA extends Base {}
    static class SubTypeB extends Base {}

    RuntimeTypeAdapterFactory<Base> factory;

    @BeforeEach
    void setup() {
        factory = RuntimeTypeAdapterFactory.of(Base.class, "type", true);
    }

    @Test
    @Timeout(8000)
    void testOf_nullBaseType_throws() {
        assertThrows(NullPointerException.class, () -> RuntimeTypeAdapterFactory.of(null, "type", true));
    }

    @Test
    @Timeout(8000)
    void testOf_nullTypeFieldName_throws() {
        assertThrows(NullPointerException.class, () -> RuntimeTypeAdapterFactory.of(Base.class, null, true));
    }

    @Test
    @Timeout(8000)
    void testRegisterSubtype_and_recognizeSubtypes() throws Exception {
        RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type");
        f.registerSubtype(SubTypeA.class, "A");
        f.registerSubtype(SubTypeB.class);
        f.recognizeSubtypes();

        // Access private fields via reflection to verify
        Field labelToSubtypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("labelToSubtype");
        labelToSubtypeField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var labelToSubtype = (java.util.Map<String, Class<?>>) labelToSubtypeField.get(f);
        assertEquals(SubTypeA.class, labelToSubtype.get("A"));
        assertTrue(labelToSubtype.containsKey("SubTypeB"));

        Field subtypeToLabelField = RuntimeTypeAdapterFactory.class.getDeclaredField("subtypeToLabel");
        subtypeToLabelField.setAccessible(true);
        @SuppressWarnings("unchecked")
        var subtypeToLabel = (java.util.Map<Class<?>, String>) subtypeToLabelField.get(f);
        assertEquals("A", subtypeToLabel.get(SubTypeA.class));
        assertNotNull(subtypeToLabel.get(SubTypeB.class));

        Field recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
        recognizeSubtypesField.setAccessible(true);
        assertTrue(recognizeSubtypesField.getBoolean(f));
    }

    @Test
    @Timeout(8000)
    void testCreate_withRegisteredSubtype() throws IOException {
        RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type");
        f.registerSubtype(SubTypeA.class, "A");

        Gson gson = mock(Gson.class);
        TypeToken<Base> typeToken = TypeToken.get(Base.class);

        TypeAdapter<Base> adapter = f.create(gson, typeToken);
        assertNotNull(adapter);
    }

    @Test
    @Timeout(8000)
    void testCreate_withUnregisteredSubtype_returnsNull() {
        RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type");
        Gson gson = mock(Gson.class);
        TypeToken<String> typeToken = TypeToken.get(String.class);

        TypeAdapter<?> adapter = f.create(gson, typeToken);
        assertNull(adapter);
    }

    @Test
    @Timeout(8000)
    void testPrivateConstructor_andFields() throws Exception {
        // Use reflection to invoke private constructor
        var ctor = RuntimeTypeAdapterFactory.class.getDeclaredConstructor(Class.class, String.class, boolean.class);
        ctor.setAccessible(true);
        RuntimeTypeAdapterFactory<Base> instance = (RuntimeTypeAdapterFactory<Base>) ctor.newInstance(Base.class, "type", false);
        assertNotNull(instance);

        Field baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
        baseTypeField.setAccessible(true);
        assertEquals(Base.class, baseTypeField.get(instance));

        Field typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
        typeFieldNameField.setAccessible(true);
        assertEquals("type", typeFieldNameField.get(instance));

        Field maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
        maintainTypeField.setAccessible(true);
        assertFalse(maintainTypeField.getBoolean(instance));
    }
}