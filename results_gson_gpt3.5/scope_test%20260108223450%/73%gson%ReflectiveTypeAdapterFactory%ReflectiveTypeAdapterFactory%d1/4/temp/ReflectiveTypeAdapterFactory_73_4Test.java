package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

class ReflectiveTypeAdapterFactory_73_4Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;

  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = new ArrayList<>();
    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
  }

  static class TestClass {
    public int publicField = 1;
    private String privateField = "private";

    public int getPublicField() {
      return publicField;
    }

    public String getPrivateField() {
      return privateField;
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withSimpleClass_shouldReturnTypeAdapter() {
    Gson gson = mock(Gson.class);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    @SuppressWarnings("unchecked")
    ObjectConstructor<TestClass> objectConstructor = mock(ObjectConstructor.class);
    // Fix: use raw type for when() to avoid generic capture issues
    when(constructorConstructor.<TestClass>get(ArgumentMatchers.<TypeToken<?>>any())).thenReturn(objectConstructor);
    when(objectConstructor.construct()).thenReturn(new TestClass());
    when(fieldNamingStrategy.translateName(any(Field.class))).thenAnswer(invocation -> {
      Field f = invocation.getArgument(0);
      return f.getName();
    });
    when(excluder.excludeClass(any(), anyBoolean())).thenReturn(false);
    when(excluder.excludeField(any(Field.class), anyBoolean())).thenReturn(false);
    // Fix: adjust mock to match method signature: getTypeAdapter(ConstructorConstructor, Gson, TypeToken<?>, JsonAdapter)
    when(jsonAdapterFactory.getTypeAdapter(any(ConstructorConstructor.class), any(Gson.class), any(TypeToken.class), any()))
        .thenReturn(null);

    TypeAdapter<TestClass> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);

    TestClass instance = new TestClass();
    assertEquals(instance.publicField, adapter.toJsonTree(instance).getAsJsonObject().get("publicField").getAsInt());
  }

  @Test
    @Timeout(8000)
  void testIncludeField_behavior() throws Exception {
    Field publicField = TestClass.class.getField("publicField");
    Field privateField = TestClass.class.getDeclaredField("privateField");

    // Exclude all fields for serialization
    when(excluder.excludeField(publicField, true)).thenReturn(true);
    when(excluder.excludeField(privateField, true)).thenReturn(true);
    // Exclude none for deserialization
    when(excluder.excludeField(publicField, false)).thenReturn(false);
    when(excluder.excludeField(privateField, false)).thenReturn(false);

    // includeField is private, invoke via reflection
    Method includeFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
    includeFieldMethod.setAccessible(true);

    boolean serializePublic = (boolean) includeFieldMethod.invoke(factory, publicField, true);
    boolean serializePrivate = (boolean) includeFieldMethod.invoke(factory, privateField, true);
    boolean deserializePublic = (boolean) includeFieldMethod.invoke(factory, publicField, false);
    boolean deserializePrivate = (boolean) includeFieldMethod.invoke(factory, privateField, false);

    assertFalse(serializePublic);
    assertFalse(serializePrivate);
    assertTrue(deserializePublic);
    assertTrue(deserializePrivate);
  }

  @Test
    @Timeout(8000)
  void testGetFieldNames_withSerializedName() throws Exception {
    class WithSerializedName {
      @com.google.gson.annotations.SerializedName(value = "foo", alternate = {"bar", "baz"})
      int field;
    }

    Field field = WithSerializedName.class.getDeclaredField("field");

    Method getFieldNamesMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    getFieldNamesMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    List<String> names = (List<String>) getFieldNamesMethod.invoke(factory, field);

    assertEquals(3, names.size());
    assertTrue(names.contains("foo"));
    assertTrue(names.contains("bar"));
    assertTrue(names.contains("baz"));
  }

  @Test
    @Timeout(8000)
  void testCheckAccessible_setsAccessible() throws Exception {
    class Dummy {
      private void privateMethod() {}
    }
    Dummy dummy = new Dummy();
    Method privateMethod = Dummy.class.getDeclaredMethod("privateMethod");

    Method checkAccessibleMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, Member.class);
    checkAccessibleMethod.setAccessible(true);

    // Before, method is not accessible
    assertFalse(privateMethod.canAccess(dummy));

    // Invoke method to set accessible
    checkAccessibleMethod.invoke(null, dummy, privateMethod);

    assertTrue(privateMethod.canAccess(dummy));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_serializationAndDeserialization() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = TestClass.class.getField("publicField");
    TypeToken<?> fieldType = TypeToken.get(int.class);

    Method accessor = TestClass.class.getMethod("getPublicField");

    Method createBoundFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField", Gson.class, Field.class, Method.class, String.class,
        TypeToken.class, boolean.class, boolean.class, boolean.class);

    createBoundFieldMethod.setAccessible(true);

    BoundField boundField = (BoundField) createBoundFieldMethod.invoke(factory,
        gson, field, accessor, "publicField", fieldType, true, true, false);

    assertNotNull(boundField);
    assertEquals("publicField", boundField.name);

    // BoundField has read and write methods, test them
    TestClass instance = new TestClass();
    // The BoundField.write method requires JsonWriter, we test read (deserialization) and write (serialization) indirectly
    // We can test that the BoundField can read and write without throwing exceptions by mocking JsonReader and JsonWriter

    // We test write by calling the write method with mocks
    com.google.gson.stream.JsonWriter jsonWriter = mock(com.google.gson.stream.JsonWriter.class);
    boundField.write(jsonWriter, instance);
    verify(jsonWriter, atLeastOnce()).name("publicField");

    // We test read by calling the read method with mocks
    com.google.gson.stream.JsonReader jsonReader = mock(com.google.gson.stream.JsonReader.class);
    when(jsonReader.peek()).thenReturn(com.google.gson.stream.JsonToken.NUMBER);
    when(jsonReader.nextInt()).thenReturn(42);

    // Fix: BoundField.read method takes JsonReader and Object (instance)
    boundField.read(jsonReader, (Object) instance);
    assertEquals(42, instance.publicField);
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_includesFields() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    when(fieldNamingStrategy.translateName(any(Field.class))).thenAnswer(invocation -> {
      Field f = invocation.getArgument(0);
      return f.getName();
    });
    when(excluder.excludeClass(any(), anyBoolean())).thenReturn(false);
    when(excluder.excludeField(any(Field.class), anyBoolean())).thenReturn(false);
    // Fix: adjust mock to match method signature: getTypeAdapter(ConstructorConstructor, Gson, TypeToken<?>, JsonAdapter)
    when(jsonAdapterFactory.getTypeAdapter(any(ConstructorConstructor.class), any(Gson.class), any(TypeToken.class), any()))
        .thenReturn(null);
    when(constructorConstructor.<TestClass>get(ArgumentMatchers.<TypeToken<?>>any())).thenReturn(mock(ObjectConstructor.class));

    Method getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    Map<String, BoundField> boundFields = (Map<String, BoundField>) getBoundFieldsMethod.invoke(
        factory, gson, typeToken, TestClass.class, false, false);

    assertNotNull(boundFields);
    assertTrue(boundFields.containsKey("publicField"));
    assertTrue(boundFields.containsKey("privateField"));
  }
}