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
import com.google.gson.JsonIOException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_withNonNullObject_callsToJsonWithClass() throws Throwable {
    StringBuilder writer = new StringBuilder();
    Object src = "testString";

    // Use public toJson(Object, Appendable) method directly (no need for reflection)
    gson.toJson(src, writer);

    // The String "testString" should be serialized as JSON string
    assertEquals("\"testString\"", writer.toString());
  }

  @Test
    @Timeout(8000)
  void toJson_withNullObject_callsToJsonWithJsonNullInstance() throws Throwable {
    StringBuilder writer = new StringBuilder();

    // Use public toJson(Object, Appendable) method directly (no need for reflection)
    gson.toJson(null, writer);

    // JsonNull.INSTANCE serializes to "null"
    assertEquals("null", writer.toString());
  }

  @Test
    @Timeout(8000)
  void toJson_withAppendableThatThrowsJsonIOException() throws IOException {
    Appendable throwingAppendable = mock(Appendable.class);
    doThrow(new IOException("IO error")).when(throwingAppendable).append(anyChar());

    // We expect JsonIOException wrapping IOException
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson("test", throwingAppendable));
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("IO error", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void toJson_invokesPrivateToJsonObjectTypeAppendable() throws Throwable {
    // Prepare parameters
    StringBuilder appendable = new StringBuilder();
    Object src = 12345;
    Type typeOfSrc = Integer.class;

    // Access private method toJson(Object, Type, Appendable)
    Method privateToJson = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, Appendable.class);
    privateToJson.setAccessible(true);

    // Invoke private method directly
    privateToJson.invoke(gson, src, typeOfSrc, appendable);

    // The integer should be serialized as JSON number string
    assertEquals("12345", appendable.toString());
  }

  @Test
    @Timeout(8000)
  void toJson_withComplexObject_serializesCorrectly() throws Throwable {
    // Create a simple POJO
    class TestPojo {
      final String name = "foo";
      final int age = 42;
    }
    TestPojo pojo = new TestPojo();
    StringBuilder writer = new StringBuilder();

    // Use public toJson(Object, Appendable) method directly (no need for reflection)
    gson.toJson(pojo, writer);

    String json = writer.toString();
    // Should contain both fields serialized
    assertTrue(json.contains("\"name\":\"foo\""));
    assertTrue(json.contains("\"age\":42"));
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonElement_callsToJsonJsonElementAppendable() throws Throwable {
    JsonElement jsonElement = new JsonPrimitive("value");
    StringBuilder writer = new StringBuilder();

    // Use public toJson(JsonElement, Appendable) method directly (no need for reflection)
    gson.toJson(jsonElement, writer);

    assertEquals("\"value\"", writer.toString());
  }
}