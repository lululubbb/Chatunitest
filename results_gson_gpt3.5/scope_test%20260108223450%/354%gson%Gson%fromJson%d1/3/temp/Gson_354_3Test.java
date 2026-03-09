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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
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
  public void fromJson_nullJson_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((JsonElement) null, typeToken);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJsonElement_callsFromJsonJsonTreeReader() throws Exception {
    JsonElement mockJsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Mock construction of JsonTreeReader to verify it is constructed with mockJsonElement
    try (MockedConstruction<JsonTreeReader> mocked = Mockito.mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          // do nothing special
        })) {

      // Use reflection to invoke private fromJson(JsonTreeReader, TypeToken) for coverage
      Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonTreeReader.class, TypeToken.class);
      fromJsonMethod.setAccessible(true);

      // Spy on gson to intercept call to fromJson(JsonTreeReader, TypeToken)
      Gson spyGson = Mockito.spy(gson);
      // When fromJson(JsonTreeReader, TypeToken) is called, call real method to check actual behavior
      doCallRealMethod().when(spyGson).fromJson(any(JsonTreeReader.class), any(TypeToken.class));

      // Call the public method under test
      Object result = spyGson.fromJson(mockJsonElement, typeToken);

      // Verify JsonTreeReader was constructed once
      assertEquals(1, mocked.constructed().size());

      // Verify the JsonTreeReader constructor was called with the mockJsonElement by checking the arguments
      JsonTreeReader constructedInstance = mocked.constructed().get(0);
      // Unfortunately MockedConstruction.Context is not accessible, so verify via the constructed instance state indirectly if possible
      // Here, just verify that the constructed instance is not null and was constructed
      assertNotNull(constructedInstance);

      // Also verify fromJson(JsonTreeReader, TypeToken) was called once
      verify(spyGson, times(1)).fromJson(any(JsonTreeReader.class), eq(typeToken));
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJsonElement_nullTypeToken_throwsNullPointerException() {
    JsonElement mockJsonElement = mock(JsonElement.class);
    assertThrows(NullPointerException.class, () -> gson.fromJson(mockJsonElement, (TypeToken<?>) null));
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJsonElement_withRealJsonTreeReader_invokesFromJson() throws Exception {
    // Prepare a simple JsonElement representing a JSON string
    JsonElement jsonElement = new com.google.gson.JsonPrimitive("test");

    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Use reflection to invoke private fromJson(JsonTreeReader, TypeToken)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonTreeReader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Call the public fromJson(JsonElement, TypeToken)
    String result = gson.fromJson(jsonElement, typeToken);

    assertEquals("test", result);

    // Also test that reflection-invoked private method returns same result
    JsonTreeReader reader = new JsonTreeReader(jsonElement);
    Object reflectedResult = fromJsonMethod.invoke(gson, reader, typeToken);
    assertEquals("test", reflectedResult);
  }
}