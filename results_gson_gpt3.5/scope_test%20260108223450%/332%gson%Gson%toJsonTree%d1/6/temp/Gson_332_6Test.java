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

import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class Gson_332_6Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_withObjectAndType() {
    String src = "testString";
    Type typeOfSrc = String.class;

    JsonElement jsonElement = gson.toJsonTree(src, typeOfSrc);

    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonPrimitive());
    assertEquals(src, jsonElement.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_withNullObject() {
    Object src = null;
    Type typeOfSrc = Object.class;

    JsonElement jsonElement = gson.toJsonTree(src, typeOfSrc);

    assertNotNull(jsonElement);
    assertTrue(jsonElement.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_invokesToJsonWithWriter() throws Exception {
    // Use reflection to test toJson method invocation inside toJsonTree
    Gson spyGson = Mockito.spy(new Gson());
    String src = "reflectionTest";
    Type typeOfSrc = String.class;

    // Call toJsonTree normally
    JsonElement result = spyGson.toJsonTree(src, typeOfSrc);

    // Verify toJson(Object, Type, JsonWriter) is called exactly once
    verify(spyGson, times(1)).toJson(eq(src), eq(typeOfSrc), any(JsonWriter.class));

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_privateMethodViaReflection() throws Exception {
    Method toJsonTreeMethod = Gson.class.getDeclaredMethod("toJsonTree", Object.class, Type.class);
    toJsonTreeMethod.setAccessible(true);

    String src = "reflectionAccess";
    Type typeOfSrc = String.class;

    Object result = toJsonTreeMethod.invoke(gson, src, typeOfSrc);

    assertNotNull(result);
    assertTrue(result instanceof JsonElement);
    assertEquals(src, ((JsonElement) result).getAsString());
  }
}