package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
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
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

class TypeAdapters_310_2Test {

  static class BaseClass {}
  static class SubClass extends BaseClass {}

  @Test
    @Timeout(8000)
  void newFactoryForMultipleTypes_create_returnsAdapterForBaseType() {
    @SuppressWarnings("unchecked")
    TypeAdapter<BaseClass> adapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newFactoryForMultipleTypes(BaseClass.class, SubClass.class, adapter);

    @SuppressWarnings("unchecked")
    TypeToken<BaseClass> baseTypeToken = (TypeToken<BaseClass>) TypeToken.get(BaseClass.class);
    TypeAdapter<BaseClass> createdAdapter = factory.create(mock(Gson.class), baseTypeToken);

    assertNotNull(createdAdapter);
    assertSame(adapter, createdAdapter);
  }

  @Test
    @Timeout(8000)
  void newFactoryForMultipleTypes_create_returnsAdapterForSubType() {
    @SuppressWarnings("unchecked")
    TypeAdapter<BaseClass> adapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newFactoryForMultipleTypes(BaseClass.class, SubClass.class, adapter);

    @SuppressWarnings("unchecked")
    TypeToken<SubClass> subTypeToken = (TypeToken<SubClass>) TypeToken.get(SubClass.class);
    TypeAdapter<SubClass> createdAdapter = factory.create(mock(Gson.class), subTypeToken);

    assertNotNull(createdAdapter);
    assertSame(adapter, createdAdapter);
  }

  @Test
    @Timeout(8000)
  void newFactoryForMultipleTypes_create_returnsNullForOtherType() {
    @SuppressWarnings("unchecked")
    TypeAdapter<BaseClass> adapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newFactoryForMultipleTypes(BaseClass.class, SubClass.class, adapter);

    @SuppressWarnings("unchecked")
    TypeToken<String> stringTypeToken = (TypeToken<String>) TypeToken.get(String.class);
    TypeAdapter<String> createdAdapter = factory.create(mock(Gson.class), stringTypeToken);

    assertNull(createdAdapter);
  }

  @Test
    @Timeout(8000)
  void newFactoryForMultipleTypes_toString_containsClassNamesAndAdapter() {
    @SuppressWarnings("unchecked")
    TypeAdapter<BaseClass> adapter = mock(TypeAdapter.class);
    when(adapter.toString()).thenReturn("mockAdapter");
    TypeAdapterFactory factory = TypeAdapters.newFactoryForMultipleTypes(BaseClass.class, SubClass.class, adapter);

    String toString = factory.toString();

    assertTrue(toString.contains(BaseClass.class.getName()));
    assertTrue(toString.contains(SubClass.class.getName()));
    assertTrue(toString.contains("mockAdapter"));
  }
}