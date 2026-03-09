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
import com.google.gson.JsonIOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void toJson_withNonNullObject_callsToJsonWithClass() throws Exception {
    StringBuilder writer = new StringBuilder();
    Object src = "testString";

    // Use reflection to spy on private toJson(Object, Type, Appendable)
    Gson spyGson = Mockito.spy(gson);

    // Call the public toJson(Object, Appendable)
    spyGson.toJson(src, writer);

    // Verify that toJson(Object, Type, Appendable) was called with correct parameters
    verify(spyGson).toJson(eq(src), eq(src.getClass()), eq(writer));
  }

  @Test
    @Timeout(8000)
  public void toJson_withNullObject_callsToJsonWithJsonNullInstance() throws Exception {
    StringBuilder writer = new StringBuilder();

    Gson spyGson = Mockito.spy(gson);

    spyGson.toJson(null, writer);

    // Verify that toJson(JsonNull.INSTANCE, Appendable) was called
    // We have to get JsonNull.INSTANCE via reflection because it's package-private
    Class<?> jsonNullClass = Class.forName("com.google.gson.JsonNull");
    Object jsonNullInstance = jsonNullClass.getField("INSTANCE").get(null);

    verify(spyGson).toJson(eq(jsonNullInstance), eq(writer));
  }

  @Test
    @Timeout(8000)
  public void toJson_privateToJson_ObjectTypeAppendable_invokesWithoutException() throws Throwable {
    String src = "hello";
    Class<?> type = String.class;
    StringBuilder writer = new StringBuilder();

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, java.lang.reflect.Type.class, Appendable.class);
    toJsonMethod.setAccessible(true);

    // Invoke private method
    toJsonMethod.invoke(gson, src, type, writer);

    // The writer should contain JSON string representation of src
    String jsonOutput = writer.toString();
    assertNotNull(jsonOutput);
    assertTrue(jsonOutput.contains("hello"));
  }

  @Test
    @Timeout(8000)
  public void toJson_privateToJson_JsonElementAppendable_invokesWithoutException() throws Throwable {
    // We need to get JsonNull.INSTANCE as JsonElement
    Class<?> jsonNullClass = Class.forName("com.google.gson.JsonNull");
    Object jsonNullInstance = jsonNullClass.getField("INSTANCE").get(null);

    StringBuilder writer = new StringBuilder();

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Class.forName("com.google.gson.JsonElement"), Appendable.class);
    toJsonMethod.setAccessible(true);

    toJsonMethod.invoke(gson, jsonNullInstance, writer);

    String jsonOutput = writer.toString();
    assertNotNull(jsonOutput);
    assertTrue(jsonOutput.contains("null"));
  }

  @Test
    @Timeout(8000)
  public void toJson_withAppendableThatThrows_throwsJsonIOException() {
    Object src = "test";

    Appendable badAppendable = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("append failed");
      }
      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("append failed");
      }
      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("append failed");
      }
    };

    assertThrows(JsonIOException.class, () -> gson.toJson(src, badAppendable));
  }
}