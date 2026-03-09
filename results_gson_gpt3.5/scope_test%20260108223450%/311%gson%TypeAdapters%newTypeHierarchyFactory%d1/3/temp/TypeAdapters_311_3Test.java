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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TypeAdapters_311_3Test {

  private Gson gson;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setup() {
    gson = mock(Gson.class);
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
  }

  static class SuperClass {}
  static class SubClass extends SuperClass {}
  static class OtherClass {}

  @Test
    @Timeout(8000)
  public void newTypeHierarchyFactory_shouldReturnNullIfNotAssignable() {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> superAdapter = (TypeAdapter<SuperClass>) mock(TypeAdapter.class);

    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    TypeToken<OtherClass> otherTypeToken = TypeToken.get(OtherClass.class);
    TypeAdapter<OtherClass> adapter = factory.create(gson, otherTypeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void newTypeHierarchyFactory_shouldDelegateWriteToProvidedAdapter() throws IOException {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> superAdapter = (TypeAdapter<SuperClass>) mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    TypeToken<SubClass> subTypeToken = TypeToken.get(SubClass.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = (TypeAdapter<SubClass>) factory.create(gson, subTypeToken);

    SubClass value = new SubClass();
    adapter.write(jsonWriter, value);

    verify(superAdapter).write(jsonWriter, value);
  }

  @Test
    @Timeout(8000)
  public void newTypeHierarchyFactory_shouldDelegateReadToProvidedAdapter() throws IOException {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> superAdapter = (TypeAdapter<SuperClass>) mock(TypeAdapter.class);
    SuperClass readValue = new SubClass();
    when(superAdapter.read(jsonReader)).thenReturn(readValue);

    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);
    TypeToken<SubClass> subTypeToken = TypeToken.get(SubClass.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = (TypeAdapter<SubClass>) factory.create(gson, subTypeToken);

    SuperClass result = adapter.read(jsonReader);

    verify(superAdapter).read(jsonReader);
    assertSame(readValue, result);
  }

  @Test
    @Timeout(8000)
  public void newTypeHierarchyFactory_readShouldThrowIfReturnedValueNotInstanceOfRequestedType() throws IOException {
    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> subAdapter = (TypeAdapter<SubClass>) mock(TypeAdapter.class);
    // Return an instance that is NOT a SubClass
    SubClass invalidValue = null;
    // We want to return a SuperClass instance but the adapter expects SubClass, so we mock a SubClass adapter that returns a SuperClass instance by using a raw TypeAdapter and casting

    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> rawSuperAdapter = (TypeAdapter<SuperClass>) mock(TypeAdapter.class);
    SuperClass invalidSuperValue = new SuperClass();
    when(rawSuperAdapter.read(jsonReader)).thenReturn(invalidSuperValue);
    // Mock getPreviousPath on JsonReader to avoid NullPointerException in exception message
    when(jsonReader.getPreviousPath()).thenReturn("$");

    // To fix the compile error, we create the factory with SubClass.class and a TypeAdapter<SubClass>
    // But we want the adapter to return an invalid SuperClass instance, so we wrap rawSuperAdapter with a cast adapter

    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SubClass.class, (TypeAdapter<SubClass>) (TypeAdapter<?>) rawSuperAdapter);
    TypeToken<SubClass> subTypeToken = TypeToken.get(SubClass.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = (TypeAdapter<SubClass>) factory.create(gson, subTypeToken);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    assertTrue(thrown.getMessage().contains("Expected a " + SubClass.class.getName()));
    assertTrue(thrown.getMessage().contains("but was " + SuperClass.class.getName()));
  }

  @Test
    @Timeout(8000)
  public void newTypeHierarchyFactory_toStringShouldContainClassAndAdapter() {
    @SuppressWarnings("unchecked")
    TypeAdapter<SuperClass> superAdapter = (TypeAdapter<SuperClass>) mock(TypeAdapter.class);

    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(SuperClass.class, superAdapter);

    String toString = factory.toString();

    assertTrue(toString.contains(SuperClass.class.getName()));
    assertTrue(toString.contains(superAdapter.toString()));
  }
}