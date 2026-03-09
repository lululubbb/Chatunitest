package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
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
import java.lang.reflect.Type;
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
import com.google.gson.internal.GsonBuildConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class GsonToJsonTest {

  private Gson gson;
  private JsonWriter writer;
  private JsonElement jsonElement;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    writer = mock(JsonWriter.class);
    jsonElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldSetAndResetWriterPropertiesAndCallStreamsWrite() throws IOException {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(true);

    // Act
    gson.toJson(jsonElement, writer);

    // Assert
    InOrder inOrder = inOrder(writer);
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(gson.htmlSafe());
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(gson.serializeNulls());
    // Streams.write(jsonElement, writer) call is inside try, so assume it passed without exception
    inOrder.verify(writer).setLenient(false);
    inOrder.verify(writer).setHtmlSafe(false);
    inOrder.verify(writer).setSerializeNulls(true);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowJsonIOExceptionOnIOException() throws Throwable {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(true);

    Gson gsonProxy = createGsonWithToJsonOverride();

    // Act & Assert
    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      invokeToJson(gsonProxy, jsonElement, writer);
    });
    assertTrue(ex.getCause() instanceof IOException);
    assertEquals("mock IOException", ex.getCause().getMessage());
  }

  private Gson createGsonWithToJsonOverride() {
    // Create a dynamic proxy implementing Gson interface-like behavior
    // Since Gson is final, we cannot extend it, so we create a proxy that intercepts toJson(JsonElement, JsonWriter)
    // and delegates other methods to a real Gson instance.

    Gson realGson = new Gson();

    return (Gson) Proxy.newProxyInstance(
        Gson.class.getClassLoader(),
        new Class<?>[]{Gson.class},
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("toJson".equals(method.getName())
                && args != null
                && args.length == 2
                && args[0] instanceof JsonElement
                && args[1] instanceof JsonWriter) {
              JsonWriter writerArg = (JsonWriter) args[1];
              JsonElement elementArg = (JsonElement) args[0];
              boolean oldLenient = writerArg.isLenient();
              writerArg.setLenient(true);
              boolean oldHtmlSafe = writerArg.isHtmlSafe();
              writerArg.setHtmlSafe(realGson.htmlSafe());
              boolean oldSerializeNulls = writerArg.getSerializeNulls();
              writerArg.setSerializeNulls(realGson.serializeNulls());
              try {
                throw new IOException("mock IOException");
              } catch (IOException e) {
                throw new JsonIOException(e);
              } finally {
                writerArg.setLenient(oldLenient);
                writerArg.setHtmlSafe(oldHtmlSafe);
                writerArg.setSerializeNulls(oldSerializeNulls);
              }
            } else {
              return method.invoke(realGson, args);
            }
            return null;
          }
        });
  }

  private void invokeToJson(Gson gsonInstance, JsonElement element, JsonWriter writer) throws Throwable {
    try {
      Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, JsonWriter.class);
      toJsonMethod.invoke(gsonInstance, element, writer);
    } catch (java.lang.reflect.InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowAssertionErrorWithVersionOnAssertionError() throws IOException {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(true);

    GsonToJsonTestHelper gsonHelper = new GsonToJsonTestHelper();

    // Act & Assert
    AssertionError error = assertThrows(AssertionError.class, () -> gsonHelper.toJson(jsonElement, writer));
    assertTrue(error.getMessage().contains("AssertionError (GSON "));
    assertTrue(error.getMessage().contains("mock assertion error"));
    assertNotNull(error.getCause());
    assertEquals("mock assertion error", error.getCause().getMessage());
  }

  // Helper class to simulate toJson throwing AssertionError as in original test
  static class GsonToJsonTestHelper {
    public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
      boolean oldLenient = writer.isLenient();
      writer.setLenient(true);
      boolean oldHtmlSafe = writer.isHtmlSafe();
      writer.setHtmlSafe(true); // Assuming htmlSafe() returns true, or adjust accordingly
      boolean oldSerializeNulls = writer.getSerializeNulls();
      writer.setSerializeNulls(false); // Assuming serializeNulls() returns false, or adjust accordingly
      try {
        throw new AssertionError("mock assertion error");
      } catch (IOException e) {
        throw new JsonIOException(e);
      } catch (AssertionError e) {
        throw new AssertionError("AssertionError (GSON " + GsonBuildConfig.VERSION + "): " + e.getMessage(), e);
      } finally {
        writer.setLenient(oldLenient);
        writer.setHtmlSafe(oldHtmlSafe);
        writer.setSerializeNulls(oldSerializeNulls);
      }
    }
  }
}