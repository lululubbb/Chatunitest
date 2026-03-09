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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = spy(new Gson());
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonElementAndClass_returnsCastedObject() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;
    Object expectedObject = "test";

    // Mock Gson#fromJson(JsonElement, TypeToken) to return expectedObject
    doReturn(expectedObject).when(gson).fromJson(eq(jsonElement), any(TypeToken.class));

    // Mock Primitives.wrap to return classOfT (for String.class it returns String.class)
    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      String result = gson.fromJson(jsonElement, classOfT);

      assertEquals(expectedObject, result);
      verify(gson).fromJson(eq(jsonElement), any(TypeToken.class));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withPrimitiveClassCastsCorrectly() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    Class<Integer> classOfT = int.class;
    Integer expectedObject = 123;

    doReturn(expectedObject).when(gson).fromJson(eq(jsonElement), any(TypeToken.class));

    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(Integer.class);

      Integer result = gson.fromJson(jsonElement, classOfT);

      assertEquals(expectedObject, result);
      verify(gson).fromJson(eq(jsonElement), any(TypeToken.class));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_nullJsonElement_returnsNull() throws Exception {
    JsonElement jsonElement = null;
    Class<String> classOfT = String.class;

    doReturn(null).when(gson).fromJson(eq(jsonElement), any(TypeToken.class));

    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      String result = gson.fromJson(jsonElement, classOfT);

      assertNull(result);
      verify(gson).fromJson(eq(jsonElement), any(TypeToken.class));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withCustomClass() throws Exception {
    class CustomClass {}

    JsonElement jsonElement = mock(JsonElement.class);
    Class<CustomClass> classOfT = CustomClass.class;
    CustomClass expectedObject = new CustomClass();

    doReturn(expectedObject).when(gson).fromJson(eq(jsonElement), any(TypeToken.class));

    try (MockedStatic<Primitives> primitivesMockedStatic = Mockito.mockStatic(Primitives.class)) {
      primitivesMockedStatic.when(() -> Primitives.wrap(classOfT)).thenReturn(classOfT);

      CustomClass result = gson.fromJson(jsonElement, classOfT);

      assertSame(expectedObject, result);
      verify(gson).fromJson(eq(jsonElement), any(TypeToken.class));
      primitivesMockedStatic.verify(() -> Primitives.wrap(classOfT));
    }
  }
}