package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_NonNullObject_AppendsJson() throws IOException {
    StringWriter writer = new StringWriter();
    String src = "test";

    gson.toJson(src, writer);

    String json = writer.toString();
    assertNotNull(json);
    assertTrue(json.contains("test"));
  }

  @Test
    @Timeout(8000)
  void toJson_NullObject_AppendsJsonNull() throws IOException {
    StringWriter writer = new StringWriter();

    gson.toJson(null, writer);

    String json = writer.toString();
    assertNotNull(json);
    assertTrue(json.contains("null"));
  }

  @Test
    @Timeout(8000)
  void toJson_InvokesPrivateToJsonSrcTypeAppendable() throws Exception {
    StringWriter writer = new StringWriter();
    String src = "privateTest";

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, Appendable.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(gson, src, src.getClass(), writer);

    String json = writer.toString();
    assertNotNull(json);
    assertTrue(json.contains("privateTest"));
  }

  @Test
    @Timeout(8000)
  void toJson_InvokesPrivateToJsonJsonElementAppendable() throws Exception {
    StringWriter writer = new StringWriter();
    JsonElement jsonElement = JsonNull.INSTANCE;

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, Appendable.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(gson, jsonElement, writer);

    String json = writer.toString();
    assertNotNull(json);
    assertTrue(json.contains("null"));
  }

  @Test
    @Timeout(8000)
  void toJson_ThrowsJsonIOException_OnIOException() throws IOException {
    Appendable writer = mock(Appendable.class);
    String src = "test";

    doThrow(new IOException("test exception")).when(writer).append(any(CharSequence.class));

    assertThrows(JsonIOException.class, () -> gson.toJson(src, writer));
  }
}