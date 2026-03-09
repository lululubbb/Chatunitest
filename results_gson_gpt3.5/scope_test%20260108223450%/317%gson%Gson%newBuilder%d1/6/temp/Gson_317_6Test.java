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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class Gson_317_6Test {

  @Test
    @Timeout(8000)
  public void testNewBuilder() {
    Gson gson = new Gson();
    GsonBuilder builder = gson.newBuilder();
    assertNotNull(builder);

    try {
      Field field = null;
      Class<?> clazz = builder.getClass();

      // Try to find a field of type Gson or a field that holds the Gson instance inside GsonBuilder
      while (clazz != null) {
        for (Field f : clazz.getDeclaredFields()) {
          // The field may be named 'gson' or similar, but type may not be Gson directly.
          // So also check if the field's value equals the gson instance.
          f.setAccessible(true);
          Object value = f.get(builder);
          if (value == gson) {
            field = f;
            break;
          }
          if (f.getType() == Gson.class) {
            field = f;
            break;
          }
        }
        if (field != null) break;
        clazz = clazz.getSuperclass();
      }

      assertNotNull(field, "Field holding Gson instance not found in GsonBuilder class hierarchy");
      field.setAccessible(true);
      Object innerGson = field.get(builder);
      assertSame(gson, innerGson);
    } catch (IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}