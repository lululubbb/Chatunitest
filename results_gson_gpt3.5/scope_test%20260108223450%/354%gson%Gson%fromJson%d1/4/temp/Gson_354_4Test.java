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

import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
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
  public void fromJson_nullJson_returnsNull() {
    JsonElement json = null;
    TypeToken<String> typeToken = TypeToken.get(String.class);

    String result = gson.fromJson(json, typeToken);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nonNullJson_callsFromJsonWithJsonTreeReader() throws Exception {
    JsonElement json = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    try (MockedConstruction<JsonTreeReader> mocked = Mockito.mockConstruction(JsonTreeReader.class)) {

      Gson spyGson = Mockito.spy(gson);

      Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonTreeReader.class, TypeToken.class);
      fromJsonMethod.setAccessible(true);

      String expected = "expectedResult";
      doReturn(expected).when(spyGson).fromJson(any(JsonTreeReader.class), eq(typeToken));

      String actual = spyGson.fromJson(json, typeToken);

      assertEquals(expected, actual);
      verify(spyGson).fromJson(any(JsonTreeReader.class), eq(typeToken));
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_privateFromJsonJsonTreeReaderTypeToken_invocation() throws Exception {
    JsonElement json = mock(JsonElement.class);
    JsonTreeReader reader = new JsonTreeReader(json);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    Method privateFromJson = Gson.class.getDeclaredMethod("fromJson", JsonTreeReader.class, TypeToken.class);
    privateFromJson.setAccessible(true);

    Gson testGson = new Gson();

    try {
      Object result = privateFromJson.invoke(testGson, reader, typeToken);
      assertNotNull(result);
    } catch (Exception e) {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof JsonSyntaxException || cause instanceof RuntimeException);
    } finally {
      reader.close();
    }
  }
}