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

class TypeAdapters_309_4Test {

  @Test
    @Timeout(8000)
  void newFactory_shouldReturnFactoryThatCreatesAdapterForUnboxedAndBoxedTypes() {
    // Arrange
    TypeAdapter<Number> mockedAdapter = mock(TypeAdapter.class);
    TypeAdapterFactory factory = TypeAdapters.newFactory(int.class, Integer.class, mockedAdapter);
    Gson gson = mock(Gson.class);

    // Act & Assert
    // For unboxed type (int.class)
    TypeAdapter<?> adapterForUnboxed = factory.create(gson, TypeToken.get(int.class));
    assertSame(mockedAdapter, adapterForUnboxed);

    // For boxed type (Integer.class)
    TypeAdapter<?> adapterForBoxed = factory.create(gson, TypeToken.get(Integer.class));
    assertSame(mockedAdapter, adapterForBoxed);

    // For unrelated type (String.class)
    TypeAdapter<?> adapterForOther = factory.create(gson, TypeToken.get(String.class));
    assertNull(adapterForOther);

    // toString returns expected string
    String toString = factory.toString();
    assertTrue(toString.contains("Factory[type="));
    assertTrue(toString.contains("adapter="));
    assertTrue(toString.contains("int"));
    assertTrue(toString.contains("Integer"));
  }
}