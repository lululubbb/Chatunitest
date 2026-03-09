package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.internal.ObjectConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ReflectiveTypeAdapterFactory_73_3Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;

  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingPolicy,
        excluder,
        jsonAdapterFactory,
        reflectionFilters);
  }

  static class TestClass {
    public int aField = 1;
    private String privateField = "private";
  }

  @Test
    @Timeout(8000)
  void testCreate_withSimpleClass() {
    Gson gson = mock(Gson.class);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    // Fix: use ArgumentMatchers and cast to ObjectConstructor<?> for generic compatibility
    when(constructorConstructor.get(ArgumentMatchers.<TypeToken<?>>any()))
        .thenAnswer(invocation -> new ObjectConstructor<TestClass>() {
          @Override
          public TestClass construct() {
            return new TestClass();
          }
        });

    // Mock excluder to not exclude fields
    when(excluder.excludeClass(TestClass.class, true)).thenReturn(false);
    when(excluder.excludeClass(TestClass.class, false)).thenReturn(false);
    when(excluder.excludeField(any(Field.class), eq(true))).thenReturn(false);
    when(excluder.excludeField(any(Field.class), eq(false))).thenReturn(false);

    // Mock fieldNamingPolicy to return the field name unchanged
    when(fieldNamingPolicy.translateName(any(Field.class))).thenAnswer(i -> ((Field) i.getArgument(0)).getName());

    // Mock gson.getAdapter(...) to avoid NullPointerException in TypeAdapterRuntimeTypeWrapper
    when(gson.getAdapter(any(TypeToken.class))).thenAnswer(invocation -> {
      TypeToken<?> token = invocation.getArgument(0);
      // Return a simple TypeAdapter that handles primitives and String for testClass fields
      return new TypeAdapter<Object>() {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, Object value) {
          // no-op for test
        }

        @Override
        public Object read(com.google.gson.stream.JsonReader in) {
          return null;
        }
      };
    });

    TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);

    TestClass instance = new TestClass();
    assertEquals(instance.aField, adapter.toJsonTree(instance).getAsJsonObject().get("aField").getAsInt());
  }

  @Test
    @Timeout(8000)
  void testIncludeField_serializationAndDeserialization() throws Exception {
    Field aField = TestClass.class.getDeclaredField("aField");

    // includeField is private, invoke by reflection
    boolean serialize = true;
    boolean deserialize = false;

    // excluder.excludeField returns false for both serialize and deserialize
    when(excluder.excludeField(aField, serialize)).thenReturn(false);
    when(excluder.excludeField(aField, deserialize)).thenReturn(false);

    // fieldNamingPolicy returns field name
    when(fieldNamingPolicy.translateName(aField)).thenReturn(aField.getName());

    // We must create a factory with mocks set properly
    ReflectiveTypeAdapterFactory f = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);

    // Use reflection to invoke includeField
    Method includeFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
    includeFieldMethod.setAccessible(true);

    boolean resultSerialize = (boolean) includeFieldMethod.invoke(f, aField, true);
    boolean resultDeserialize = (boolean) includeFieldMethod.invoke(f, aField, false);

    assertTrue(resultSerialize);
    assertTrue(resultDeserialize);
  }

  @Test
    @Timeout(8000)
  void testGetFieldNames_returnsSerializedNames() throws Exception {
    Field aField = TestClass.class.getDeclaredField("aField");

    ReflectiveTypeAdapterFactory f = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);

    // Mock fieldNamingPolicy to return "renamed"
    when(fieldNamingPolicy.translateName(aField)).thenReturn("renamed");

    // Use reflection to invoke getFieldNames
    Method getFieldNamesMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    getFieldNamesMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    List<String> names = (List<String>) getFieldNamesMethod.invoke(f, aField);

    assertNotNull(names);
    assertTrue(names.contains("renamed"));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_blockInaccessibleFalse() throws Exception {
    Field aField = TestClass.class.getDeclaredField("aField");
    TypeToken<?> fieldType = TypeToken.get(int.class);

    ReflectiveTypeAdapterFactory f = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);

    Method createBoundFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField",
        Gson.class, Field.class, Method.class, String.class,
        TypeToken.class, boolean.class, boolean.class, boolean.class);
    createBoundFieldMethod.setAccessible(true);

    BoundField boundField = (BoundField) createBoundFieldMethod.invoke(
        f,
        mock(Gson.class),
        aField,
        null,
        "aField",
        fieldType,
        true,
        true,
        false);

    assertNotNull(boundField);
    assertEquals("aField", boundField.name);
  }

  @Test
    @Timeout(8000)
  void testCheckAccessible_setsAccessible() throws Exception {
    // The method checkAccessible is private static and generic with type parameter M extends Member
    // The actual method signature is:
    // private static <M extends Member> void checkAccessible(Object object, M member)
    // So to find it via reflection, we need to find the method with two parameters: Object and Member

    Method checkAccessibleMethod = null;
    for (Method m : ReflectiveTypeAdapterFactory.class.getDeclaredMethods()) {
      if (m.getName().equals("checkAccessible")) {
        Class<?>[] params = m.getParameterTypes();
        if (params.length == 2 && params[0] == Object.class && Member.class.isAssignableFrom(params[1])) {
          checkAccessibleMethod = m;
          break;
        }
      }
    }
    assertNotNull(checkAccessibleMethod, "checkAccessible method not found");
    checkAccessibleMethod.setAccessible(true);

    Field field = TestClass.class.getDeclaredField("privateField");
    Object dummyObject = new Object();
    // Before calling checkAccessible, field.canAccess(dummyObject) is false
    assertFalse(field.canAccess(dummyObject));

    // Invoke checkAccessible with dummyObject and field (which is a Member)
    checkAccessibleMethod.invoke(null, dummyObject, field);

    // After calling checkAccessible, field.canAccess(dummyObject) should be true
    assertTrue(field.canAccess(dummyObject));
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_withSimpleClass() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    ReflectiveTypeAdapterFactory f = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, reflectionFilters);

    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    // Mock excluder to not exclude fields
    when(excluder.excludeClass(TestClass.class, true)).thenReturn(false);
    when(excluder.excludeClass(TestClass.class, false)).thenReturn(false);
    when(excluder.excludeField(any(Field.class), eq(true))).thenReturn(false);
    when(excluder.excludeField(any(Field.class), eq(false))).thenReturn(false);

    // Mock fieldNamingPolicy to return the field name unchanged
    when(fieldNamingPolicy.translateName(any(Field.class))).thenAnswer(i -> ((Field) i.getArgument(0)).getName());

    @SuppressWarnings("unchecked")
    Map<String, BoundField> boundFields = (Map<String, BoundField>) getBoundFieldsMethod.invoke(
        f,
        gson,
        typeToken,
        TestClass.class,
        false,
        false);

    assertNotNull(boundFields);
    assertTrue(boundFields.containsKey("aField"));
  }
}