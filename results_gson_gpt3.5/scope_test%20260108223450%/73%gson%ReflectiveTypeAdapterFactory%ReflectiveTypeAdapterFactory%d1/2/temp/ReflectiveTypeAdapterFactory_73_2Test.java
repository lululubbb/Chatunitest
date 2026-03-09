package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.util.*;

class ReflectiveTypeAdapterFactory_73_2Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<Object> reflectionFilters;
  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  void setUp() throws Exception {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    // Use reflection to get the nested ReflectionAccessFilter class and create an empty list of that type
    Class<?> reflectionAccessFilterClass = Class.forName("com.google.gson.internal.ReflectiveTypeAdapterFactory$ReflectionAccessFilter");
    // Create an empty list with the proper generic type using a raw list cast
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);
  }

  static class TestClass {
    @SuppressWarnings("unused")
    public int publicField = 1;
    @SuppressWarnings("unused")
    private String privateField = "private";
    @SuppressWarnings("unused")
    protected double protectedField = 2.0;
    @SuppressWarnings("unused")
    transient int transientField = 5;
  }

  @Test
    @Timeout(8000)
  void testCreate_withSimpleClass() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    ObjectConstructor<TestClass> objectConstructor = mock(ObjectConstructor.class);
    when(constructorConstructor.get(ArgumentMatchers.<TypeToken<?>>any())).thenReturn((ObjectConstructor) objectConstructor);
    when(objectConstructor.construct()).thenReturn(new TestClass());

    when(fieldNamingPolicy.translateName(any(Field.class))).thenAnswer(i -> ((Field) i.getArgument(0)).getName());

    when(excluder.excludeClass(any(), anyBoolean())).thenReturn(false);
    when(excluder.excludeField(any(Field.class), anyBoolean())).thenReturn(false);

    TypeAdapter<?> stringAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn((TypeAdapter) stringAdapter);
    TypeAdapter<?> intAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(int.class))).thenReturn((TypeAdapter) intAdapter);
    TypeAdapter<?> doubleAdapter = mock(TypeAdapter.class);
    when(gson.getAdapter(TypeToken.get(double.class))).thenReturn((TypeAdapter) doubleAdapter);

    @SuppressWarnings("unchecked")
    TypeAdapter<TestClass> adapter = (TypeAdapter<TestClass>) factory.create(gson, typeToken);
    assertNotNull(adapter);

    TestClass instance = new TestClass();
    JsonWriter writer = new JsonWriter(mock(java.io.Writer.class));
    // We won't test actual write/read logic deeply here because it depends on other adapters

    // Test that adapter is not null and can write/read without exceptions (assuming mocks)
    try {
      adapter.write(writer, instance);
    } catch (IOException ignored) {
    }

    JsonReader reader = new JsonReader(mock(java.io.Reader.class));
    try {
      adapter.read(reader);
    } catch (IOException ignored) {
    }
  }

  @Test
    @Timeout(8000)
  void testIncludeField_behavior() throws Exception {
    Field publicField = TestClass.class.getField("publicField");
    Field privateField = TestClass.class.getDeclaredField("privateField");

    // excluder excludes nothing
    when(excluder.excludeField(publicField, true)).thenReturn(false);
    when(excluder.excludeField(privateField, true)).thenReturn(false);

    // No reflection filters
    ReflectiveTypeAdapterFactory localFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, Collections.emptyList());

    // includeField is private, use reflection to invoke
    Method includeFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
    includeFieldMethod.setAccessible(true);

    // Serialize true
    boolean includePublic = (boolean) includeFieldMethod.invoke(localFactory, publicField, true);
    boolean includePrivate = (boolean) includeFieldMethod.invoke(localFactory, privateField, true);

    assertTrue(includePublic);
    assertTrue(includePrivate);

    // Now simulate excluder excludes private field
    when(excluder.excludeField(privateField, true)).thenReturn(true);
    boolean includePrivateExcluded = (boolean) includeFieldMethod.invoke(localFactory, privateField, true);
    assertFalse(includePrivateExcluded);
  }

  @Test
    @Timeout(8000)
  void testGetFieldNames_withSerializedName() throws Exception {
    class SerializedNameClass {
      @com.google.gson.annotations.SerializedName(value = "customName", alternate = {"alt1", "alt2"})
      public int field;
    }
    Field field = SerializedNameClass.class.getField("field");

    Method getFieldNamesMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    getFieldNamesMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    List<String> names = (List<String>) getFieldNamesMethod.invoke(factory, field);

    assertEquals(3, names.size());
    assertEquals("customName", names.get(0));
    assertTrue(names.contains("alt1"));
    assertTrue(names.contains("alt2"));
  }

  @Test
    @Timeout(8000)
  void testCheckAccessible_setsAccessible() throws Exception {
    Method checkAccessibleMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, Member.class);
    checkAccessibleMethod.setAccessible(true);

    Field field = TestClass.class.getDeclaredField("privateField");
    field.setAccessible(false);

    checkAccessibleMethod.invoke(null, factory, field);

    assertTrue(field.canAccess(new TestClass()));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_serializationDeserialization() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = TestClass.class.getField("publicField");
    Method accessor = null;
    String name = "publicField";
    TypeToken<?> fieldType = TypeToken.get(int.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Integer> intAdapter = (TypeAdapter<Integer>) mock(TypeAdapter.class);
    when(gson.getAdapter(fieldType)).thenReturn((TypeAdapter) intAdapter);

    Method createBoundFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
      "createBoundField", Gson.class, Field.class, Method.class, String.class,
      TypeToken.class, boolean.class, boolean.class, boolean.class);
    createBoundFieldMethod.setAccessible(true);

    Object boundField = createBoundFieldMethod.invoke(factory, gson, field, accessor, name, fieldType, true, true, true);
    assertNotNull(boundField);
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_withRecord() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
      "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    Map<String, ?> boundFields = (Map<String, ?>) getBoundFieldsMethod.invoke(factory, gson, typeToken, TestClass.class, true, false);
    assertNotNull(boundFields);
    assertTrue(boundFields.containsKey("publicField"));
  }
}