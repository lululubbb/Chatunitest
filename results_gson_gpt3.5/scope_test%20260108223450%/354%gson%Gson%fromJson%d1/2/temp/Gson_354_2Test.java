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
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
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
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

class Gson_FromJson_JsonElement_TypeToken_Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_returnsNull() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((JsonElement) null, typeToken);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nonNullJson_invokesFromJsonJsonTreeReaderTypeToken() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Spy on gson to verify interaction with fromJson(JsonReader, TypeToken)
    Gson spyGson = Mockito.spy(gson);

    // Use reflection to get the fromJson(JsonReader, TypeToken) method to avoid ambiguity
    Method fromJsonReaderMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonReaderMethod.setAccessible(true);

    // We will stub the fromJson(JsonReader, TypeToken) to return a known value
    String expected = "expectedResult";
    doReturn(expected).when(spyGson).fromJson(any(JsonReader.class), eq(typeToken));

    // Act
    String actual = spyGson.fromJson(jsonElement, typeToken);

    // Assert
    assertEquals(expected, actual);

    // Verify fromJson(JsonReader, TypeToken) called with JsonTreeReader wrapping jsonElement
    verify(spyGson).fromJson(argThat(reader -> {
      if (!(reader instanceof JsonTreeReader)) return false;
      try {
        var jsonField = JsonTreeReader.class.getDeclaredField("json");
        jsonField.setAccessible(true);
        Object innerJson = jsonField.get(reader);
        return innerJson == jsonElement;
      } catch (Exception e) {
        return false;
      }
    }), eq(typeToken));
  }
}