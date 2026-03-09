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

import java.lang.reflect.Method;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_NullJson_ReturnsNull() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((JsonElement) null, typeToken);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_ValidJsonElement_DelegatesToFromJsonJsonTreeReader() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    Method fromJsonJsonTreeReaderMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonJsonTreeReaderMethod.setAccessible(true);

    Gson spyGson = spy(gson);
    doReturn("expectedResult").when(spyGson).fromJson(any(JsonTreeReader.class), eq(typeToken));

    Object result = fromJsonJsonTreeReaderMethod.invoke(spyGson, new JsonTreeReader(jsonElement), typeToken);
    assertEquals("expectedResult", result);

    String actualResult = spyGson.fromJson(jsonElement, typeToken);
    assertEquals("expectedResult", actualResult);
  }

  @Test
    @Timeout(8000)
  void fromJson_JsonTreeReaderThrowsJsonSyntaxException_Propagates() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    Gson spyGson = spy(gson);

    doThrow(new JsonSyntaxException("syntax error"))
        .when(spyGson).fromJson(any(JsonTreeReader.class), eq(typeToken));

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(jsonElement, typeToken);
    });
    assertEquals("syntax error", thrown.getMessage());
  }

}