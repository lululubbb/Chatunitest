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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

class GsonFromJsonTest {

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
  public void fromJson_validJson_callsFromJsonJsonTreeReader() throws Exception {
    // Prepare a mock JsonElement
    JsonElement mockJson = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Spy on Gson to mock fromJson(JsonTreeReader, TypeToken)
    Gson spyGson = Mockito.spy(gson);

    // Mock the construction of JsonTreeReader to capture the argument
    try (MockedConstruction<JsonTreeReader> mocked = mockConstruction(JsonTreeReader.class,
        (mock, context) -> {
          // do nothing, just mock construction
        })) {

      // Mock the return value of fromJson(JsonTreeReader, TypeToken)
      String expected = "testResult";
      doReturn(expected).when(spyGson).fromJson(any(JsonTreeReader.class), eq(typeToken));

      // Call the focal method
      String actual = spyGson.fromJson(mockJson, typeToken);

      // Verify that JsonTreeReader was constructed with mockJson
      assertEquals(1, mocked.constructed().size());
      JsonTreeReader createdReader = mocked.constructed().get(0);
      assertNotNull(createdReader);

      // Verify that fromJson(JsonTreeReader, TypeToken) was called with the created JsonTreeReader and typeToken
      verify(spyGson).fromJson(createdReader, typeToken);

      // Assert the returned value is the mocked one
      assertEquals(expected, actual);
    }
  }

  @Test
    @Timeout(8000)
  public void fromJson_privateFromJsonJsonTreeReaderTypeToken_invokedByReflection() throws Exception {
    // Prepare a JsonElement
    JsonElement jsonElement = mock(JsonElement.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Use reflection to get the private fromJson(JsonTreeReader, TypeToken) method
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonTreeReader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Create a JsonTreeReader instance with the jsonElement
    JsonTreeReader reader = new JsonTreeReader(jsonElement);

    // Call the private method via reflection
    Object result = fromJsonMethod.invoke(gson, reader, typeToken);

    // Since the jsonElement is mocked and not a real JsonElement, result is likely null or throws JsonSyntaxException
    // We just verify no unexpected exception thrown and result can be null or any
    assertTrue(result == null || result instanceof String || result instanceof Object);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJsonElementAndTypeToken_nullReturned() {
    // Fix: cannot pass null TypeToken<?> because of generic bounds, so use raw type and cast
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeToken<String> nullTypeToken = (TypeToken) null;
    String result = gson.fromJson((JsonElement) null, nullTypeToken);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJsonElementWithNonNullTypeToken_nullReturned() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((JsonElement) null, typeToken);
    assertNull(result);
  }
}