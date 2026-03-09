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
import org.mockito.Mockito;

public class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_givenJsonElementAndClass_returnsCastObject() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<String> classOfT = String.class;
    String expectedObject = "test";

    TypeToken<String> typeToken = TypeToken.get(classOfT);

    // Spy on gson to mock fromJson(JsonElement, TypeToken<T>)
    Gson spyGson = Mockito.spy(gson);

    // Use reflection to mock the fromJson method with TypeToken parameter
    doReturn(expectedObject).when(spyGson).fromJson(eq(jsonElement), eq(typeToken));

    // Act
    String actualObject = spyGson.fromJson(jsonElement, classOfT);

    // Assert
    assertSame(expectedObject, actualObject);

    // Verify internal calls
    verify(spyGson).fromJson(jsonElement, typeToken);
  }

  @Test
    @Timeout(8000)
  public void fromJson_givenNullJsonElement_returnsNull() throws Exception {
    // Arrange
    JsonElement jsonElement = null;
    Class<String> classOfT = String.class;

    TypeToken<String> typeToken = TypeToken.get(classOfT);

    // Spy on gson to mock fromJson(JsonElement, TypeToken<T>) returning null
    Gson spyGson = Mockito.spy(gson);
    doReturn(null).when(spyGson).fromJson(eq(jsonElement), eq(typeToken));

    // Act
    String actualObject = spyGson.fromJson(jsonElement, classOfT);

    // Assert
    assertNull(actualObject);

    verify(spyGson).fromJson(jsonElement, typeToken);
  }

  @Test
    @Timeout(8000)
  public void fromJson_givenPrimitiveClass_castsCorrectly() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Class<Integer> primitiveClass = int.class;
    Integer expectedObject = 123;

    TypeToken<Integer> typeToken = TypeToken.get(primitiveClass);

    Gson spyGson = Mockito.spy(gson);
    doReturn(expectedObject).when(spyGson).fromJson(eq(jsonElement), eq(typeToken));

    // Act
    Integer actualObject = spyGson.fromJson(jsonElement, primitiveClass);

    // Assert
    assertEquals(expectedObject, actualObject);

    verify(spyGson).fromJson(jsonElement, typeToken);
  }
}