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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_withNonNullObject_callsToJsonWithClass() throws Exception {
    // Arrange
    Object src = "test string";
    Appendable writer = mock(Appendable.class);

    // Spy on Gson instance
    Gson spyGson = spy(gson);

    // Use reflection to get the toJson(Object, Type, Appendable) method
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, Appendable.class);
    toJsonMethod.setAccessible(true);

    // Stub the toJson(Object, Type, Appendable) method to do nothing
    doAnswer(invocation -> null)
        .when(spyGson)
        .toJson(
            any(),
            any(Type.class),
            any(Appendable.class)
        );

    // Act
    spyGson.toJson(src, writer);

    // Assert
    verify(spyGson).toJson(src, src.getClass(), writer);
  }

  @Test
    @Timeout(8000)
  void toJson_withNullObject_callsToJsonWithJsonNullInstance() throws Exception {
    // Arrange
    Appendable writer = mock(Appendable.class);

    // Spy on Gson to verify call to toJson(JsonNull.INSTANCE, Appendable)
    Gson spyGson = spy(gson);

    // Use reflection to access private static JsonNull.INSTANCE
    Class<?> jsonNullClass = Class.forName("com.google.gson.JsonNull");
    Field instanceField = jsonNullClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);
    Object jsonNullInstance = instanceField.get(null);

    // Stub the toJson(JsonElement, Appendable) method to do nothing
    doAnswer(invocation -> null)
        .when(spyGson)
        .toJson(any(JsonElement.class), any(Appendable.class));

    // Act
    spyGson.toJson(null, writer);

    // Assert
    verify(spyGson).toJson((JsonElement) jsonNullInstance, writer);
  }

  @Test
    @Timeout(8000)
  void toJson_withNullObject_andWriterThrows_throwsJsonIOException() {
    // Arrange
    Appendable writer = mock(Appendable.class);
    try {
      doThrow(new IOException("append error")).when(writer).append(any(CharSequence.class));
    } catch (IOException e) {
      fail("Setup failed: " + e);
    }

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson(null, writer));
    assertEquals("append error", thrown.getCause().getMessage());
  }
}