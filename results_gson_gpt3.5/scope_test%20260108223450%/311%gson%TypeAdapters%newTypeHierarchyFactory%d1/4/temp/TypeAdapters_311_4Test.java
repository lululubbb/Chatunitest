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
import org.mockito.InOrder;

class TypeAdapters_311_4Test {

  private Gson gson;
  private JsonWriter jsonWriter;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    gson = mock(Gson.class);
    jsonWriter = mock(JsonWriter.class);
    jsonReader = mock(JsonReader.class);
  }

  static class SuperClass {}
  static class SubClass extends SuperClass {}

  @Test
    @Timeout(8000)
  void create_shouldReturnNullIfNotAssignable() {
    TypeAdapter<SuperClass> superAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    TypeToken<String> stringType = TypeToken.get(String.class);
    // String is not assignable from SuperClass
    TypeAdapter<String> adapter = factory.create(gson, stringType);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapterThatDelegatesWriteAndRead() throws IOException {
    TypeAdapter<SuperClass> superAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    TypeToken<SubClass> subType = TypeToken.get(SubClass.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = factory.create(gson, subType);
    assertNotNull(adapter);

    SubClass subInstance = new SubClass();

    // Test write delegates to original adapter
    adapter.write(jsonWriter, subInstance);
    verify(superAdapter).write(jsonWriter, subInstance);

    // Test read delegates to original adapter and returns result
    when(superAdapter.read(jsonReader)).thenReturn(subInstance);
    SubClass readResult = adapter.read(jsonReader);
    assertSame(subInstance, readResult);
  }

  @Test
    @Timeout(8000)
  void create_shouldThrowJsonSyntaxExceptionIfReadReturnsWrongType() throws IOException {
    TypeAdapter<SuperClass> superAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    TypeToken<SubClass> subType = TypeToken.get(SubClass.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = factory.create(gson, subType);
    assertNotNull(adapter);

    // Return an instance of SuperClass but requestedType is SubClass
    SuperClass wrongInstance = new SuperClass();
    when(superAdapter.read(jsonReader)).thenReturn(wrongInstance);
    when(jsonReader.getPreviousPath()).thenReturn("$.somePath");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    assertTrue(ex.getMessage().contains(SubClass.class.getName()));
    assertTrue(ex.getMessage().contains(SuperClass.class.getName()));
    assertTrue(ex.getMessage().contains("$.somePath"));
  }

  @Test
    @Timeout(8000)
  void toString_shouldContainClassNameAndAdapter() {
    TypeAdapter<SuperClass> superAdapter = mock(TypeAdapter.class);
    when(superAdapter.toString()).thenReturn("SuperAdapterMock");
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    String str = factory.toString();
    assertTrue(str.contains(SuperClass.class.getName()));
    assertTrue(str.contains("SuperAdapterMock"));
  }
}