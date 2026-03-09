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
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_NullJson_ReturnsNull() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((JsonElement) null, typeToken);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_ValidJsonElement_DelegatesToFromJsonWithJsonTreeReader() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    try (MockedConstruction<JsonTreeReader> mocked = mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          // no behavior needed on JsonTreeReader
        })) {

      Gson spyGson = spy(gson);

      doReturn("mockedResult").when(spyGson).fromJson(any(JsonReader.class), eq(typeToken));

      String result = spyGson.fromJson(jsonElement, typeToken);

      assertEquals(1, mocked.constructed().size());
      JsonTreeReader createdReader = mocked.constructed().get(0);

      verify(spyGson).fromJson(any(JsonReader.class), eq(typeToken));
      assertEquals("mockedResult", result);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_NullJsonElement_ThrowsJsonSyntaxException_IfFromJsonThrows() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    Gson spyGson = spy(gson);

    doThrow(new JsonSyntaxException("syntax error"))
        .when(spyGson).fromJson(any(JsonReader.class), eq(typeToken));

    assertThrows(JsonSyntaxException.class, () -> spyGson.fromJson(jsonElement, typeToken));
  }

}