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
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withJsonElementAndClass_shouldReturnProperInstance() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;

    // Use reflection to access Primitives.wrap(Class)
    Method wrapMethod = null;
    try {
      Class<?> primitivesClass = Class.forName("com.google.gson.internal.Primitives");
      wrapMethod = primitivesClass.getMethod("wrap", Class.class);
    } catch (ClassNotFoundException e) {
      fail("Primitives class not found");
    }

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenMockedStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      // Spy on gson to mock fromJson(json, TypeToken)
      Gson spyGson = spy(gson);
      String expectedObject = "test";
      doReturn(expectedObject).when(spyGson).fromJson(jsonElement, typeToken);

      // Mock Primitives.wrap(classOfT) by reflection
      Class<?> wrappedClass = (Class<?>) wrapMethod.invoke(null, classOfT);

      // Call the method under test
      String actualObject = spyGson.fromJson(jsonElement, classOfT);

      // Verify the returned object is cast properly
      assertSame(expectedObject, actualObject);

      // Verify interactions
      typeTokenMockedStatic.verify(() -> TypeToken.get(classOfT));
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_withPrimitiveClass_shouldWrapAndCast() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    Class<Integer> classOfT = int.class;

    // Use reflection to access Primitives.wrap(Class)
    Method wrapMethod = null;
    try {
      Class<?> primitivesClass = Class.forName("com.google.gson.internal.Primitives");
      wrapMethod = primitivesClass.getMethod("wrap", Class.class);
    } catch (ClassNotFoundException e) {
      fail("Primitives class not found");
    }

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class)) {

      Class<?> wrappedClass = (Class<?>) wrapMethod.invoke(null, classOfT);

      @SuppressWarnings("unchecked")
      TypeToken<Integer> typeToken = (TypeToken<Integer>) TypeToken.get(wrappedClass);

      typeTokenMockedStatic.when(() -> TypeToken.get(wrappedClass)).thenReturn(typeToken);

      Gson spyGson = spy(gson);
      Integer expectedObject = 42;
      doReturn(expectedObject).when(spyGson).fromJson(jsonElement, typeToken);

      Integer actualObject = spyGson.fromJson(jsonElement, classOfT);

      assertSame(expectedObject, actualObject);

      typeTokenMockedStatic.verify(() -> TypeToken.get(wrappedClass));
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullJsonElement_shouldReturnNull() throws Exception {
    JsonElement jsonElement = null;
    Class<String> classOfT = String.class;

    // Use reflection to access Primitives.wrap(Class)
    Method wrapMethod = null;
    try {
      Class<?> primitivesClass = Class.forName("com.google.gson.internal.Primitives");
      wrapMethod = primitivesClass.getMethod("wrap", Class.class);
    } catch (ClassNotFoundException e) {
      fail("Primitives class not found");
    }

    try (MockedStatic<TypeToken> typeTokenMockedStatic = Mockito.mockStatic(TypeToken.class)) {

      TypeToken<String> typeToken = TypeToken.get(classOfT);
      typeTokenMockedStatic.when(() -> TypeToken.get(classOfT)).thenReturn(typeToken);

      Gson spyGson = spy(gson);
      doReturn(null).when(spyGson).fromJson(jsonElement, typeToken);

      String actualObject = spyGson.fromJson(jsonElement, classOfT);

      assertNull(actualObject);

      typeTokenMockedStatic.verify(() -> TypeToken.get(classOfT));
      verify(spyGson).fromJson(jsonElement, typeToken);
    }
  }
}