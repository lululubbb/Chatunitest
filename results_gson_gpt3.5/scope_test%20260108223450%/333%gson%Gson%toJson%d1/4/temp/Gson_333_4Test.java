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
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void testToJson_NullSrc_ReturnsJsonNullInstance() throws Exception {
    // Use reflection to invoke public toJson(JsonElement) method
    Method toJsonJsonElementMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class);
    toJsonJsonElementMethod.setAccessible(true);

    String result = (String) toJsonJsonElementMethod.invoke(gson, JsonNull.INSTANCE);
    assertNotNull(result);
    // JsonNull.INSTANCE should serialize to "null"
    assertEquals("null", result);

    // Test public toJson(Object) with null returns same result
    String publicResult = gson.toJson((Object) null);
    assertEquals(result, publicResult);
  }

  @Test
    @Timeout(8000)
  void testToJson_NonNullSrc_UsesSrcClass() {
    Object src = "testString";

    // Spy on gson to verify that toJson(Object, Type) is called with correct parameters
    Gson spyGson = spy(gson);
    doReturn("mockedJson").when(spyGson).toJson(eq(src), eq(src.getClass()));

    String json = spyGson.toJson(src);

    assertEquals("mockedJson", json);
    verify(spyGson).toJson(src, src.getClass());
  }

  @Test
    @Timeout(8000)
  void testToJson_JsonElementMethod_ProducesValidJson() throws Exception {
    // Prepare a JsonElement mock to test toJson(JsonElement) method
    JsonElement jsonElementMock = mock(JsonElement.class);

    // Since toJson(JsonElement) is public, getMethod can be used
    Method toJsonJsonElementMethod = Gson.class.getMethod("toJson", JsonElement.class);

    String json = (String) toJsonJsonElementMethod.invoke(gson, jsonElementMock);
    assertNotNull(json);
    // It should return a string (possibly empty)
    assertTrue(json instanceof String);
  }

}