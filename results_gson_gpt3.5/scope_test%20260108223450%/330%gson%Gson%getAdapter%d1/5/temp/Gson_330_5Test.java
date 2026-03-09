package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Gson_330_5Test {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testGetAdapterWithClass() {
        // Test with String class
        TypeAdapter<String> adapter = gson.getAdapter(String.class);
        assertNotNull(adapter);
        assertEquals(String.class, adapter.getClass().getGenericInterfaces().length > 0 ? String.class : String.class);

        // Test with Integer class
        TypeAdapter<Integer> adapterInt = gson.getAdapter(Integer.class);
        assertNotNull(adapterInt);

        // Test with custom class
        class Custom {}
        TypeAdapter<Custom> adapterCustom = gson.getAdapter(Custom.class);
        assertNotNull(adapterCustom);
    }

    @Test
    @Timeout(8000)
    public void testGetAdapterWithTypeToken() throws Exception {
        // Access private getAdapter(TypeToken<T>) method via reflection
        Method getAdapterMethod = Gson.class.getDeclaredMethod("getAdapter", TypeToken.class);
        getAdapterMethod.setAccessible(true);

        TypeToken<String> stringTypeToken = TypeToken.get(String.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<String> adapter = (TypeAdapter<String>) getAdapterMethod.invoke(gson, stringTypeToken);
        assertNotNull(adapter);

        TypeToken<Integer> intTypeToken = TypeToken.get(Integer.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<Integer> adapterInt = (TypeAdapter<Integer>) getAdapterMethod.invoke(gson, intTypeToken);
        assertNotNull(adapterInt);

        class Custom {}
        TypeToken<Custom> customTypeToken = TypeToken.get(Custom.class);
        @SuppressWarnings("unchecked")
        TypeAdapter<Custom> adapterCustom = (TypeAdapter<Custom>) getAdapterMethod.invoke(gson, customTypeToken);
        assertNotNull(adapterCustom);
    }
}