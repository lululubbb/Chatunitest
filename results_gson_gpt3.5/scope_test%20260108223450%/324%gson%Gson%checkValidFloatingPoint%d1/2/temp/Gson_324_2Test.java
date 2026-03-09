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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class Gson_checkValidFloatingPoint_Test {

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_validFiniteValues_noException() {
    assertDoesNotThrow(() -> Gson.checkValidFloatingPoint(0.0));
    assertDoesNotThrow(() -> Gson.checkValidFloatingPoint(1.0));
    assertDoesNotThrow(() -> Gson.checkValidFloatingPoint(-1.0));
    assertDoesNotThrow(() -> Gson.checkValidFloatingPoint(Double.MIN_NORMAL));
    assertDoesNotThrow(() -> Gson.checkValidFloatingPoint(Double.MAX_VALUE));
    assertDoesNotThrow(() -> Gson.checkValidFloatingPoint(Double.MIN_VALUE));
  }

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_NaN_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      Gson.checkValidFloatingPoint(Double.NaN);
    });
  }

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_PositiveInfinity_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      Gson.checkValidFloatingPoint(Double.POSITIVE_INFINITY);
    });
  }

  @Test
    @Timeout(8000)
  void checkValidFloatingPoint_NegativeInfinity_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      Gson.checkValidFloatingPoint(Double.NEGATIVE_INFINITY);
    });
  }
}