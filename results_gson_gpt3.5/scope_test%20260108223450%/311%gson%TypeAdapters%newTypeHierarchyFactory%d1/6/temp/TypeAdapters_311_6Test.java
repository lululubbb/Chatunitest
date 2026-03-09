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

public class TypeAdapters_311_6Test {

  private Gson gson;
  private JsonReader jsonReader;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    gson = mock(Gson.class);
    jsonReader = mock(JsonReader.class);
    jsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_create_returnsNullIfNotAssignable() {
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(Number.class, (TypeAdapter) stringAdapter);

    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    // String is not a subclass of Number, so create should return null
    assertNull(factory.create(gson, stringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_create_returnsTypeAdapterAndDelegatesWriteAndRead() throws IOException {
    // Arrange
    class Base {}
    class Derived extends Base {}

    @SuppressWarnings("unchecked")
    TypeAdapter<Base> baseAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(Base.class, baseAdapter);

    TypeToken<Derived> derivedTypeToken = TypeToken.get(Derived.class);

    // Act
    @SuppressWarnings("unchecked")
    TypeAdapter<Derived> adapter = (TypeAdapter<Derived>) factory.create(gson, derivedTypeToken);

    assertNotNull(adapter);

    Derived derivedInstance = new Derived();

    // Write delegates to baseAdapter.write
    adapter.write(jsonWriter, derivedInstance);
    verify(baseAdapter).write(jsonWriter, derivedInstance);

    // Read delegates to baseAdapter.read and returns the result if instance matches
    when(baseAdapter.read(jsonReader)).thenReturn(derivedInstance);
    Derived readResult = adapter.read(jsonReader);
    assertSame(derivedInstance, readResult);

    // Verify read calls baseAdapter.read
    verify(baseAdapter).read(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_create_readThrowsJsonSyntaxExceptionIfWrongType() throws IOException {
    class Base {}
    class Derived extends Base {}
    class Other {}

    @SuppressWarnings("unchecked")
    TypeAdapter<Base> baseAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(Base.class, baseAdapter);

    TypeToken<Derived> derivedTypeToken = TypeToken.get(Derived.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Derived> adapter = (TypeAdapter<Derived>) factory.create(gson, derivedTypeToken);

    Other otherInstance = new Other();

    // Return Object (Other) without casting to Base to avoid compile error
    when(baseAdapter.read(jsonReader)).thenReturn((Base) null);
    // Use Answer to return Other instance bypassing static typing
    doAnswer(invocation -> otherInstance).when(baseAdapter).read(jsonReader);

    when(jsonReader.getPreviousPath()).thenReturn("$");

    JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> {
      adapter.read(jsonReader);
    });

    String expectedMsg = "Expected a " + Derived.class.getName() + " but was " + Other.class.getName() + "; at path $";
    assertEquals(expectedMsg, exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testNewTypeHierarchyFactory_toString() {
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(String.class, stringAdapter);

    String toString = factory.toString();
    assertTrue(toString.contains("Factory[typeHierarchy=" + String.class.getName()));
    assertTrue(toString.contains("adapter="));
  }
}