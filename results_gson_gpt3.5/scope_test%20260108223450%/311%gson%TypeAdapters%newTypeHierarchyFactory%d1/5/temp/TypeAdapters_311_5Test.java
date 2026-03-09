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
import java.lang.reflect.Field;

public class TypeAdapters_311_5Test {

  private Gson gsonMock;
  private JsonWriter jsonWriterMock;
  private JsonReader jsonReaderMock;

  @BeforeEach
  public void setUp() {
    gsonMock = mock(Gson.class);
    jsonWriterMock = mock(JsonWriter.class);
    jsonReaderMock = mock(JsonReader.class);
  }

  static class BaseClass {}
  static class SubClass extends BaseClass {}
  static class OtherClass {}

  @Test
    @Timeout(8000)
  public void testCreate_withAssignableType_callsWriteAndReadSuccessfully() throws IOException {
    TypeAdapter<BaseClass> baseAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(BaseClass.class, baseAdapter);

    TypeToken<SubClass> subClassTypeToken = TypeToken.get(SubClass.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = factory.create(gsonMock, subClassTypeToken);
    assertNotNull(adapter);

    SubClass subInstance = new SubClass();

    // Test write delegates to baseAdapter.write
    adapter.write(jsonWriterMock, subInstance);
    verify(baseAdapter).write(jsonWriterMock, subInstance);

    // Setup read to return a valid instance of SubClass (actually BaseClass reference)
    when(baseAdapter.read(jsonReaderMock)).thenReturn(subInstance);

    SubClass readResult = adapter.read(jsonReaderMock);
    assertSame(subInstance, readResult);

    // Setup read to return null
    when(baseAdapter.read(jsonReaderMock)).thenReturn(null);
    assertNull(adapter.read(jsonReaderMock));
  }

  @Test
    @Timeout(8000)
  public void testCreate_withNonAssignableType_returnsNull() {
    TypeAdapter<BaseClass> baseAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(BaseClass.class, baseAdapter);

    TypeToken<OtherClass> otherTypeToken = TypeToken.get(OtherClass.class);

    TypeAdapter<OtherClass> adapter = factory.create(gsonMock, otherTypeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testRead_throwsJsonSyntaxException_whenResultNotInstanceOfRequestedType() throws IOException {
    TypeAdapter<BaseClass> baseAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(BaseClass.class, baseAdapter);

    TypeToken<SubClass> subClassTypeToken = TypeToken.get(SubClass.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<SubClass> adapter = factory.create(gsonMock, subClassTypeToken);
    assertNotNull(adapter);

    // Return a BaseClass instance which is not instanceof SubClass (BaseClass is superclass of SubClass)
    BaseClass baseInstance = new BaseClass();
    when(baseAdapter.read(jsonReaderMock)).thenReturn(baseInstance);

    // Use reflection to mock 'previousPath' field on jsonReaderMock
    try {
      Field previousPathField = JsonReader.class.getDeclaredField("previousPath");
      previousPathField.setAccessible(true);
      previousPathField.set(jsonReaderMock, "$.somePath");
    } catch (NoSuchFieldException | IllegalAccessException e) {
      // fallback: do nothing, test might fail if previousPath is not set
    }

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReaderMock));
    assertTrue(ex.getMessage().contains("Expected a " + SubClass.class.getName()));
    assertTrue(ex.getMessage().contains("but was " + BaseClass.class.getName()));
    assertTrue(ex.getMessage().contains("$.somePath"));
  }

  @Test
    @Timeout(8000)
  public void testToString_containsClassNameAndAdapter() {
    TypeAdapter<BaseClass> baseAdapter = mock(TypeAdapter.class);
    when(baseAdapter.toString()).thenReturn("MockBaseAdapter");

    TypeAdapterFactory factory = TypeAdapters.newTypeHierarchyFactory(BaseClass.class, baseAdapter);
    String str = factory.toString();

    assertTrue(str.contains(BaseClass.class.getName()));
    assertTrue(str.contains("MockBaseAdapter"));
  }
}