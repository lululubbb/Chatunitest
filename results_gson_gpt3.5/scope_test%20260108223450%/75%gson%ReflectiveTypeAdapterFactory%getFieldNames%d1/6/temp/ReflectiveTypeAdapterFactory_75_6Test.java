package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_75_6Test {

  ReflectiveTypeAdapterFactory factory;
  FieldNamingStrategy fieldNamingStrategy;

  static class TestClass {
    @SerializedName("annotatedName")
    String annotatedField;

    @SerializedName(value = "primary", alternate = {"alt1", "alt2"})
    String annotatedFieldWithAlternates;

    String noAnnotationField;
  }

  @BeforeEach
  void setUp() {
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    // constructorConstructor, excluder, jsonAdapterFactory, reflectionFilters can be null for this test
    factory = new ReflectiveTypeAdapterFactory(null, fieldNamingStrategy, null, null, null);
  }

  @Test
    @Timeout(8000)
  void getFieldNames_noAnnotation_returnsFieldNamingStrategyName() throws Exception {
    Field field = TestClass.class.getDeclaredField("noAnnotationField");
    when(fieldNamingStrategy.translateName(field)).thenReturn("translatedName");

    List<String> result = invokeGetFieldNames(factory, field);

    assertEquals(1, result.size());
    assertEquals("translatedName", result.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedNameNoAlternates_returnsSingleName() throws Exception {
    Field field = TestClass.class.getDeclaredField("annotatedField");

    List<String> result = invokeGetFieldNames(factory, field);

    assertEquals(1, result.size());
    assertEquals("annotatedName", result.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedNameWithAlternates_returnsAllNames() throws Exception {
    Field field = TestClass.class.getDeclaredField("annotatedFieldWithAlternates");

    List<String> result = invokeGetFieldNames(factory, field);

    assertEquals(3, result.size());
    assertEquals("primary", result.get(0));
    assertTrue(result.contains("alt1"));
    assertTrue(result.contains("alt2"));
  }

  @SuppressWarnings("unchecked")
  private List<String> invokeGetFieldNames(ReflectiveTypeAdapterFactory factory, Field field) throws Exception {
    java.lang.reflect.Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    method.setAccessible(true);
    return (List<String>) method.invoke(factory, field);
  }
}