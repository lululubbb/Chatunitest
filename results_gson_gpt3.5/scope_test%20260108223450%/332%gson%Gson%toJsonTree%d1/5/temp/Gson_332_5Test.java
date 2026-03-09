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
import com.google.gson.reflect.TypeToken;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonToJsonTreeTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_withNullObject() {
    JsonElement result = gson.toJsonTree(null, Object.class);
    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_withPrimitive() {
    Integer src = 123;
    JsonElement result = gson.toJsonTree(src, Integer.class);
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(src.intValue(), result.getAsInt());
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_withString() {
    String src = "test string";
    JsonElement result = gson.toJsonTree(src, String.class);
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(src, result.getAsString());
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_withObjectType() {
    Object src = new TestObject("foo", 42);
    JsonElement result = gson.toJsonTree(src, TestObject.class);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals("foo", result.getAsJsonObject().get("name").getAsString());
    assertEquals(42, result.getAsJsonObject().get("value").getAsInt());
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_invokesPrivateToJson() throws Exception {
    // Spy on gson instance
    Gson spyGson = spy(gson);
    Object src = "reflection test";
    Type type = String.class;

    // Use reflection to get private method toJson
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, com.google.gson.stream.JsonWriter.class);
    toJsonMethod.setAccessible(true);

    // Create JsonTreeWriter to capture output
    JsonTreeWriter writer = new JsonTreeWriter();

    // Call toJson directly via reflection to verify it works
    toJsonMethod.invoke(spyGson, src, type, writer);
    JsonElement element = writer.get();
    assertTrue(element.isJsonPrimitive());
    assertEquals(src, element.getAsString());

    // Stub toJsonTree to call real method
    doCallRealMethod().when(spyGson).toJsonTree(any(), any());

    // Call toJson with a spy writer to verify interaction
    JsonTreeWriter spyWriter = spy(new JsonTreeWriter());
    toJsonMethod.invoke(spyGson, src, type, spyWriter);
    verify(spyWriter, atLeastOnce()).beginObject(); // verify some interaction on spyWriter

    // Now call toJsonTree and verify it returns the same JsonElement as the direct toJson call
    JsonElement result = spyGson.toJsonTree(src, type);
    assertEquals(element, result);
  }

  private static class TestObject {
    String name;
    int value;

    TestObject(String name, int value) {
      this.name = name;
      this.value = value;
    }
  }
}