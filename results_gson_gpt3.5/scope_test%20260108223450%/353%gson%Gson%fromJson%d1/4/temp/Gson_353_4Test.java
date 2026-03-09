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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withJsonElementAndType_callsFromJsonWithTypeToken() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Type type = String.class;

    // Spy on gson to verify internal call
    Gson spyGson = Mockito.spy(gson);

    // Cast to the fromJson method with TypeToken parameter to disambiguate
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    doReturn("mockedResult").when(spyGson).fromJson(
        eq(jsonElement),
        argThat(argument -> {
          if (argument == null) return false;
          TypeToken<?> typeToken = (TypeToken<?>) argument;
          return typeToken.getType().equals(type);
        }));

    // Act
    Object result = fromJsonInvoke(spyGson, jsonElement, type);

    // Assert
    assertEquals("mockedResult", result);
    verify(spyGson).fromJson(
        eq(jsonElement),
        argThat(argument -> {
          if (argument == null) return false;
          TypeToken<?> typeToken = (TypeToken<?>) argument;
          return typeToken.getType().equals(type);
        }));
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJsonElement_returnsNull() {
    JsonElement jsonElement = null;
    Type type = String.class;

    String result = gson.fromJson(jsonElement, type);

    assertNull(result);
  }

  // Helper method to invoke the focal method fromJson(JsonElement json, Type typeOfT)
  @SuppressWarnings("unchecked")
  private <T> T fromJsonInvoke(Gson gsonInstance, JsonElement json, Type type) throws Exception {
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, Type.class);
    fromJsonMethod.setAccessible(true);
    return (T) fromJsonMethod.invoke(gsonInstance, json, type);
  }
}