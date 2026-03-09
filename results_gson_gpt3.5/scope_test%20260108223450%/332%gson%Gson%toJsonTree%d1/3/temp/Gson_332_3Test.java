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
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

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
  void toJsonTree_withNullSrc_returnsJsonNull() throws Exception {
    JsonElement result = gson.toJsonTree(null, Object.class);
    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_withPrimitiveSrc_returnsJsonPrimitive() throws Exception {
    Integer src = 123;
    JsonElement result = gson.toJsonTree(src, Integer.class);
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(src, result.getAsInt());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_withStringSrc_returnsJsonPrimitive() throws Exception {
    String src = "test";
    JsonElement result = gson.toJsonTree(src, String.class);
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(src, result.getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_withObjectSrc_returnsJsonObject() throws Exception {
    class TestObj {
      String field = "value";
    }
    TestObj obj = new TestObj();
    JsonElement result = gson.toJsonTree(obj, TestObj.class);
    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals("value", result.getAsJsonObject().get("field").getAsString());
  }

  @Test
    @Timeout(8000)
  void toJsonTree_invokesToJsonWithJsonTreeWriter() throws Exception {
    try (MockedConstruction<JsonTreeWriter> mocked = Mockito.mockConstruction(JsonTreeWriter.class,
        (mock, context) -> {
          when(mock.get()).thenReturn(JsonNull.INSTANCE);
        })) {
      JsonElement result = gson.toJsonTree("abc", String.class);
      assertNotNull(result);
      assertEquals(JsonNull.INSTANCE, result);
      assertEquals(1, mocked.constructed().size());
      JsonTreeWriter writer = mocked.constructed().get(0);
      verify(writer).get();
    }
  }

  @Test
    @Timeout(8000)
  void toJsonTree_usesReflectionToInvokePrivateToJson() throws Exception {
    Gson spyGson = spy(gson);
    JsonTreeWriter writer = new JsonTreeWriter();

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);

    toJsonMethod.invoke(spyGson, "abc", (Type) String.class, writer);
    JsonElement json = writer.get();
    assertNotNull(json);
    assertTrue(json.isJsonPrimitive());
    assertEquals("abc", json.getAsString());
  }
}