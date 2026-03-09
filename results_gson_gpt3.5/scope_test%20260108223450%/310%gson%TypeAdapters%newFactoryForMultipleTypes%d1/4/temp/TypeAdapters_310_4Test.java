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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeAdapters_310_4Test {

  private Class<BaseClass> baseClass;
  private Class<? extends BaseClass> subClass;
  private TypeAdapter<BaseClass> typeAdapter;
  private TypeAdapterFactory factory;

  static class BaseClass {}
  static class SubClass extends BaseClass {}
  static class OtherClass {}

  @BeforeEach
  void setUp() {
    baseClass = BaseClass.class;
    subClass = SubClass.class;
    typeAdapter = mock(TypeAdapter.class);
    factory = TypeAdapters.newFactoryForMultipleTypes(baseClass, subClass, typeAdapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsTypeAdapter_forBaseClass() {
    Gson gson = mock(Gson.class);
    TypeToken<BaseClass> typeToken = TypeToken.get(baseClass);

    TypeAdapter<BaseClass> adapter = factory.create(gson, typeToken);

    assertSame(typeAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsTypeAdapter_forSubClass() {
    Gson gson = mock(Gson.class);
    // Fix: use a cast to suppress incompatible type inference
    @SuppressWarnings("unchecked")
    TypeToken<SubClass> typeToken = (TypeToken<SubClass>) (TypeToken<?>) TypeToken.get(subClass);

    TypeAdapter<SubClass> adapter = factory.create(gson, typeToken);

    assertSame(typeAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  void create_returnsNull_forOtherClass() {
    Gson gson = mock(Gson.class);
    TypeToken<OtherClass> typeToken = TypeToken.get(OtherClass.class);

    TypeAdapter<?> adapter = factory.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void toString_containsBaseAndSubClassNames_andTypeAdapter() {
    String str = factory.toString();

    assertTrue(str.contains(baseClass.getName()));
    assertTrue(str.contains(subClass.getName()));
    assertTrue(str.contains(typeAdapter.toString()));
  }
}