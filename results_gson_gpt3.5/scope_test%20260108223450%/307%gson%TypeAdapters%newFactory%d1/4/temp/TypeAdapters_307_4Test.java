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

class TypeAdapters_307_4Test {

  @Test
    @Timeout(8000)
  void newFactory_create_returnsAdapterForMatchingType() {
    // Arrange
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> mockAdapter = mock(TypeAdapter.class);

    TypeAdapterFactory factory = TypeAdapters.newFactory(stringTypeToken, mockAdapter);

    Gson gson = mock(Gson.class);

    // Act
    TypeAdapter<String> adapterForSameType = factory.create(gson, stringTypeToken);
    TypeAdapter<Integer> adapterForDifferentType = factory.create(gson, TypeToken.get(Integer.class));

    // Assert
    assertNotNull(adapterForSameType);
    assertSame(mockAdapter, adapterForSameType);

    assertNull(adapterForDifferentType);
  }
}