package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Currency;
import java.util.Deque;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapters_311_1Test {

  private TypeAdapter<String> mockTypeAdapter;
  private Gson mockGson;
  private JsonWriter mockJsonWriter;
  private JsonReader mockJsonReader;

  @BeforeEach
  void setUp() {
    mockTypeAdapter = mock(TypeAdapter.class);
    mockGson = mock(Gson.class);
    mockJsonWriter = mock(JsonWriter.class);
    mockJsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void testCreate_withAssignableType_writeDelegatesToTypeAdapter() throws IOException {
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(String.class, mockTypeAdapter);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = factory.create(mockGson, typeToken);
    assertNotNull(adapter);

    String value = "testValue";
    adapter.write(mockJsonWriter, value);

    // verify write delegates to mockTypeAdapter with the original value
    verify(mockTypeAdapter).write(mockJsonWriter, value);
  }

  @Test
    @Timeout(8000)
  void testCreate_withNonAssignableType_returnsNull() {
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(String.class, mockTypeAdapter);
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    TypeAdapter<Integer> adapter = factory.create(mockGson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testRead_withCorrectInstanceType_returnsResult() throws IOException {
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(CharSequence.class, (TypeAdapter) mockTypeAdapter);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = factory.create(mockGson, typeToken);
    assertNotNull(adapter);

    String expected = "result";
    when(mockTypeAdapter.read(mockJsonReader)).thenReturn(expected);

    String result = adapter.read(mockJsonReader);
    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  void testRead_withNullResult_returnsNull() throws IOException {
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(CharSequence.class, (TypeAdapter) mockTypeAdapter);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = factory.create(mockGson, typeToken);
    assertNotNull(adapter);

    when(mockTypeAdapter.read(mockJsonReader)).thenReturn(null);

    String result = adapter.read(mockJsonReader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_withIncorrectInstanceType_throwsJsonSyntaxException() throws IOException {
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(CharSequence.class, (TypeAdapter) mockTypeAdapter);
    TypeToken<StringBuilder> typeToken = TypeToken.get(StringBuilder.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<StringBuilder> adapter = factory.create(mockGson, typeToken);
    assertNotNull(adapter);

    Object wrongInstance = new Object();
    // Return an Object that is NOT a String to avoid ClassCastException
    when(mockTypeAdapter.read(mockJsonReader)).thenReturn((String) null);
    // Use doAnswer to simulate returning wrongInstance after casting
    doAnswer(invocation -> wrongInstance).when(mockTypeAdapter).read(mockJsonReader);
    when(mockJsonReader.getPreviousPath()).thenReturn("$");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      adapter.read(mockJsonReader);
    });
    assertTrue(thrown.getMessage().contains("Expected a " + StringBuilder.class.getName()));
    assertTrue(thrown.getMessage().contains("but was " + wrongInstance.getClass().getName()));
  }

  @Test
    @Timeout(8000)
  void testToString_returnsExpectedString() {
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(String.class, mockTypeAdapter);
    String toString = factory.toString();
    assertTrue(toString.contains("Factory[typeHierarchy=" + String.class.getName()));
    assertTrue(toString.contains("adapter="));
  }
}