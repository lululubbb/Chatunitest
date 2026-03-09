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
import java.io.StringWriter;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

class GsonToJsonTest {

  private Gson gson;
  private JsonWriter jsonWriter;
  private Type typeOfSrc;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();
    jsonWriter = mock(JsonWriter.class);
    typeOfSrc = String.class; // using String class as example type

    // Set gson.htmlSafe and gson.serializeNulls to false via reflection to match mocks
    setFinalField(gson, "htmlSafe", false);
    setFinalField(gson, "serializeNulls", false);
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(target, value);
  }

  @Test
    @Timeout(8000)
  void testToJson_NormalBehavior() throws Exception {
    String src = "test string";

    // Setup JsonWriter mocks for lenient, htmlSafe, serializeNulls getters and setters
    when(jsonWriter.isLenient()).thenReturn(false);
    when(jsonWriter.isHtmlSafe()).thenReturn(false);
    when(jsonWriter.getSerializeNulls()).thenReturn(false);

    // Spy on Gson to mock getAdapter(TypeToken)
    Gson spyGson = Mockito.spy(gson);

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    // Call toJson
    spyGson.toJson(src, typeOfSrc, jsonWriter);

    // Verify writer setters called with correct values and restored
    InOrder inOrder = inOrder(jsonWriter, adapter);
    inOrder.verify(jsonWriter).isLenient();
    inOrder.verify(jsonWriter).setLenient(true);
    inOrder.verify(jsonWriter).isHtmlSafe();
    inOrder.verify(jsonWriter).setHtmlSafe(false);
    inOrder.verify(jsonWriter).getSerializeNulls();
    inOrder.verify(jsonWriter).setSerializeNulls(false);
    inOrder.verify(adapter).write(jsonWriter, src);
    inOrder.verify(jsonWriter).setLenient(false);
    inOrder.verify(jsonWriter).setHtmlSafe(false);
    inOrder.verify(jsonWriter).setSerializeNulls(false);
  }

  @Test
    @Timeout(8000)
  void testToJson_IOExceptionThrown() throws Exception {
    String src = "test string";

    when(jsonWriter.isLenient()).thenReturn(false);
    when(jsonWriter.isHtmlSafe()).thenReturn(false);
    when(jsonWriter.getSerializeNulls()).thenReturn(false);

    Gson spyGson = Mockito.spy(gson);
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    // Make adapter.write throw IOException
    doThrow(new IOException("IO error")).when(adapter).write(jsonWriter, src);

    JsonIOException thrown = assertThrows(JsonIOException.class,
        () -> spyGson.toJson(src, typeOfSrc, jsonWriter));
    assertEquals("java.io.IOException: IO error", thrown.getCause().toString());

    // Verify writer setters called and restored
    InOrder inOrder = inOrder(jsonWriter, adapter);
    inOrder.verify(jsonWriter).isLenient();
    inOrder.verify(jsonWriter).setLenient(true);
    inOrder.verify(jsonWriter).isHtmlSafe();
    inOrder.verify(jsonWriter).setHtmlSafe(false);
    inOrder.verify(jsonWriter).getSerializeNulls();
    inOrder.verify(jsonWriter).setSerializeNulls(false);
    inOrder.verify(adapter).write(jsonWriter, src);
    inOrder.verify(jsonWriter).setLenient(false);
    inOrder.verify(jsonWriter).setHtmlSafe(false);
    inOrder.verify(jsonWriter).setSerializeNulls(false);
  }

  @Test
    @Timeout(8000)
  void testToJson_AssertionErrorThrown() throws Exception {
    String src = "test string";

    when(jsonWriter.isLenient()).thenReturn(false);
    when(jsonWriter.isHtmlSafe()).thenReturn(false);
    when(jsonWriter.getSerializeNulls()).thenReturn(false);

    Gson spyGson = Mockito.spy(gson);
    @SuppressWarnings("unchecked")
    TypeAdapter<Object> adapter = mock(TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    // Make adapter.write throw AssertionError
    AssertionError error = new AssertionError("test assertion error");
    doThrow(error).when(adapter).write(jsonWriter, src);

    AssertionError thrown = assertThrows(AssertionError.class,
        () -> spyGson.toJson(src, typeOfSrc, jsonWriter));
    assertTrue(thrown.getMessage().contains("AssertionError (GSON"));
    assertEquals(error, thrown.getCause());

    // Verify writer setters called and restored
    InOrder inOrder = inOrder(jsonWriter, adapter);
    inOrder.verify(jsonWriter).isLenient();
    inOrder.verify(jsonWriter).setLenient(true);
    inOrder.verify(jsonWriter).isHtmlSafe();
    inOrder.verify(jsonWriter).setHtmlSafe(false);
    inOrder.verify(jsonWriter).getSerializeNulls();
    inOrder.verify(jsonWriter).setSerializeNulls(false);
    inOrder.verify(adapter).write(jsonWriter, src);
    inOrder.verify(jsonWriter).setLenient(false);
    inOrder.verify(jsonWriter).setHtmlSafe(false);
    inOrder.verify(jsonWriter).setSerializeNulls(false);
  }
}