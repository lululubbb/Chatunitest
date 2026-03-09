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
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GsonCheckValidFloatingPointTest {

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_validFiniteValues_noException() throws Exception {
    java.lang.reflect.Method method = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    method.setAccessible(true);

    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, 0.0d);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, -1.0d);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, 1.0d);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, Double.MIN_NORMAL);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, Double.MAX_VALUE);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, 123456.789d);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, -98765.4321d);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_nan_throwsIllegalArgumentException() throws Exception {
    java.lang.reflect.Method method = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(null, Double.NaN);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    String message = thrown.getMessage();
    org.junit.jupiter.api.Assertions.assertTrue(message.contains("NaN") || message.contains("nan"));
    org.junit.jupiter.api.Assertions.assertTrue(message.contains("not a valid double value"));
  }

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_positiveInfinity_throwsIllegalArgumentException() throws Exception {
    java.lang.reflect.Method method = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(null, Double.POSITIVE_INFINITY);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    String message = thrown.getMessage();
    org.junit.jupiter.api.Assertions.assertTrue(message.contains("Infinity"));
    org.junit.jupiter.api.Assertions.assertTrue(message.contains("not a valid double value"));
  }

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_negativeInfinity_throwsIllegalArgumentException() throws Exception {
    java.lang.reflect.Method method = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(null, Double.NEGATIVE_INFINITY);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    String message = thrown.getMessage();
    org.junit.jupiter.api.Assertions.assertTrue(message.contains("-Infinity") || message.contains("Infinity"));
    org.junit.jupiter.api.Assertions.assertTrue(message.contains("not a valid double value"));
  }
}