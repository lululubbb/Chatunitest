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

class TypeAdapters_307_3Test {

  @Test
    @Timeout(8000)
  void newFactory_create_returnsAdapterWhenTypeMatches() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = mock(TypeAdapter.class);

    TypeAdapterFactory factory = TypeAdapters.newFactory(typeToken, adapter);

    Gson gson = mock(Gson.class);
    TypeAdapter<String> createdAdapter = factory.create(gson, typeToken);

    assertSame(adapter, createdAdapter, "Factory should return the adapter when types match");
  }

  @Test
    @Timeout(8000)
  void newFactory_create_returnsNullWhenTypeDoesNotMatch() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> adapter = mock(TypeAdapter.class);

    TypeAdapterFactory factory = TypeAdapters.newFactory(typeToken, adapter);

    Gson gson = mock(Gson.class);
    TypeToken<Integer> differentTypeToken = TypeToken.get(Integer.class);
    TypeAdapter<?> createdAdapter = factory.create(gson, differentTypeToken);

    assertNull(createdAdapter, "Factory should return null when types do not match");
  }
}