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
import org.junit.jupiter.api.Test;

class TypeAdapters_311_2Test {

  static class SuperClass {}
  static class SubClass extends SuperClass {}
  static class OtherClass {}

  @Test
    @Timeout(8000)
  void testNewTypeHierarchyFactory_withAssignableType_writeAndRead() throws IOException {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> mockedAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, mockedAdapter);

    Gson gson = mock(Gson.class);
    TypeToken<SubClass> subClassTypeToken = TypeToken.get(SubClass.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = (TypeAdapter<SubClass>) factory.create(gson, subClassTypeToken);
    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    SubClass subInstance = new SubClass();
    adapter.write(jsonWriter, subInstance);
    verify(mockedAdapter).write(jsonWriter, subInstance);

    JsonReader jsonReader = mock(JsonReader.class);
    SubClass returnedInstance = new SubClass();
    when(mockedAdapter.read(jsonReader)).thenReturn(returnedInstance);
    SubClass readResult = adapter.read(jsonReader);
    assertSame(returnedInstance, readResult);
  }

  @Test
    @Timeout(8000)
  void testNewTypeHierarchyFactory_withNonAssignableType_returnsNull() {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> mockedAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, mockedAdapter);

    Gson gson = mock(Gson.class);
    TypeToken<OtherClass> otherClassTypeToken = TypeToken.get(OtherClass.class);

    TypeAdapter<?> adapter = factory.create(gson, otherClassTypeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testNewTypeHierarchyFactory_readReturnsWrongType_throwsJsonSyntaxException() throws IOException {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> mockedAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, mockedAdapter);

    Gson gson = mock(Gson.class);
    TypeToken<SubClass> subClassTypeToken = TypeToken.get(SubClass.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = (TypeAdapter<SubClass>) factory.create(gson, subClassTypeToken);
    assertNotNull(adapter);

    JsonReader jsonReader = mock(JsonReader.class);
    // Create a SuperClass instance that is NOT an instance of SubClass
    SuperClass wrongInstance = new SuperClass();

    when(mockedAdapter.read(jsonReader)).thenReturn(wrongInstance);
    when(jsonReader.getPreviousPath()).thenReturn("$");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    assertTrue(ex.getMessage().contains("Expected a " + SubClass.class.getName()));
    assertTrue(ex.getMessage().contains("but was " + wrongInstance.getClass().getName()));
    assertTrue(ex.getMessage().contains("at path $"));
  }

  @Test
    @Timeout(8000)
  void testToString_returnsExpectedString() {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> mockedAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, mockedAdapter);

    String toString = factory.toString();
    assertTrue(toString.contains("Factory[typeHierarchy=" + SuperClass.class.getName()));
    assertTrue(toString.contains("adapter="));
  }
}