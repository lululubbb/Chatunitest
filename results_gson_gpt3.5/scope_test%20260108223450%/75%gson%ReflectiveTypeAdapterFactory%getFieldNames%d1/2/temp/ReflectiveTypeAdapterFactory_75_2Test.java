package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_75_2Test {

  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    // Mock dependencies required by constructor
    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    FieldNamingStrategy fieldNamingStrategy = mock(FieldNamingStrategy.class);
    Excluder excluder = mock(Excluder.class);
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    List<ReflectionAccessFilter> reflectionFilters = List.of();

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        reflectionFilters
    );
  }

  static class TestClassNoAnnotation {
    public int age;
  }

  static class TestClassWithSerializedName {
    @SerializedName("custom_name")
    public String name;
  }

  static class TestClassWithSerializedNameAlternates {
    @SerializedName(value = "mainName", alternate = {"alt1", "alt2"})
    public String field;
  }

  @Test
    @Timeout(8000)
  void getFieldNames_returnsFieldNamingPolicyName_whenNoSerializedNameAnnotation() throws Exception {
    Field field = TestClassNoAnnotation.class.getField("age");

    // Mock fieldNamingPolicy.translateName to return a custom name
    FieldNamingStrategy fieldNamingStrategy = getFieldNamingStrategy(factory);
    when(fieldNamingStrategy.translateName(field)).thenReturn("translatedAge");

    List<String> names = invokeGetFieldNames(factory, field);

    assertNotNull(names);
    assertEquals(1, names.size());
    assertEquals("translatedAge", names.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_returnsSerializedNameValue_whenAnnotationWithoutAlternates() throws Exception {
    Field field = TestClassWithSerializedName.class.getField("name");

    List<String> names = invokeGetFieldNames(factory, field);

    assertNotNull(names);
    assertEquals(1, names.size());
    assertEquals("custom_name", names.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_returnsSerializedNameValueAndAlternates_whenAnnotationWithAlternates() throws Exception {
    Field field = TestClassWithSerializedNameAlternates.class.getField("field");

    List<String> names = invokeGetFieldNames(factory, field);

    assertNotNull(names);
    assertEquals(3, names.size());
    assertEquals("mainName", names.get(0));
    assertEquals("alt1", names.get(1));
    assertEquals("alt2", names.get(2));
  }

  // Helper method to invoke private getFieldNames using reflection
  @SuppressWarnings("unchecked")
  private static List<String> invokeGetFieldNames(ReflectiveTypeAdapterFactory factory, Field field) throws Exception {
    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    method.setAccessible(true);
    return (List<String>) method.invoke(factory, field);
  }

  // Helper to get the mocked fieldNamingPolicy from the factory instance
  private static FieldNamingStrategy getFieldNamingStrategy(ReflectiveTypeAdapterFactory factory) {
    try {
      var field = ReflectiveTypeAdapterFactory.class.getDeclaredField("fieldNamingPolicy");
      field.setAccessible(true);
      return (FieldNamingStrategy) field.get(factory);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}