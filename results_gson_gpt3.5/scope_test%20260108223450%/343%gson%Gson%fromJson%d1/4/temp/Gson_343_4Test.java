package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Gson_FromJson_String_Class_Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_returnsObject() throws Exception {
    String json = "{\"value\":123}";
    Class<Dummy> classOfT = Dummy.class;

    // Spy gson to mock fromJson(String, TypeToken) call
    Gson spyGson = Mockito.spy(gson);
    Dummy dummyInstance = new Dummy();
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);
      doReturn(dummyInstance).when(spyGson).fromJson(eq(json), eq(TypeToken.get(classOfT)));

      Dummy result = spyGson.fromJson(json, classOfT);

      assertSame(dummyInstance, result);
      verify(spyGson).fromJson(eq(json), eq(TypeToken.get(classOfT)));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_returnsNull() throws Exception {
    String json = null;
    Class<Dummy> classOfT = Dummy.class;

    Gson spyGson = Mockito.spy(gson);
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);
      doReturn(null).when(spyGson).fromJson(eq(json), eq(TypeToken.get(classOfT)));

      Dummy result = spyGson.fromJson(json, classOfT);

      assertNull(result);
      verify(spyGson).fromJson(eq(json), eq(TypeToken.get(classOfT)));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_invalidJson_throwsJsonSyntaxException() throws Exception {
    String json = "invalid json";
    Class<Dummy> classOfT = Dummy.class;

    Gson spyGson = Mockito.spy(gson);
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);
      doThrow(new JsonSyntaxException("syntax error")).when(spyGson).fromJson(eq(json), eq(TypeToken.get(classOfT)));

      JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> spyGson.fromJson(json, classOfT));
      verify(spyGson).fromJson(eq(json), eq(TypeToken.get(classOfT)));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }

  // Dummy class for testing
  private static class Dummy {
    int value;
  }
}