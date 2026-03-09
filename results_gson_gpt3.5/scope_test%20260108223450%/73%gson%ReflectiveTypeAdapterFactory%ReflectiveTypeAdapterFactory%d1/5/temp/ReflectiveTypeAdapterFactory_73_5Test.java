package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_73_5Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<com.google.gson.ReflectionAccessFilter> reflectionFilters;

  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();

    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void testCreate_withNullTypeAdapter() {
    Gson gson = mock(Gson.class);
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Mock ConstructorConstructor to return an ObjectConstructor
    when(constructorConstructor.get(typeToken)).thenReturn(() -> "constructed");

    // Mock Excluder to not exclude any fields
    when(excluder.excludeClass(String.class, true)).thenReturn(false);
    when(excluder.excludeClass(String.class, false)).thenReturn(false);

    // Mock FieldNamingStrategy to return field name as is
    when(fieldNamingStrategy.translateName(any(Field.class))).thenAnswer(invocation -> {
      Field field = invocation.getArgument(0);
      return field.getName();
    });

    TypeAdapter<String> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testIncludeField_trueAndFalse() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("field");
    // includeField is private, use reflection to invoke
    boolean serializeIncluded = invokeIncludeField(field, true);
    boolean deserializeIncluded = invokeIncludeField(field, false);
    assertTrue(serializeIncluded);
    assertTrue(deserializeIncluded);

    Field transientField = SampleClass.class.getDeclaredField("transientField");
    boolean serializeTransient = invokeIncludeField(transientField, true);
    boolean deserializeTransient = invokeIncludeField(transientField, false);
    assertFalse(serializeTransient);
    assertFalse(deserializeTransient);
  }

  @Test
    @Timeout(8000)
  void testGetFieldNames_singleAndMultiple() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("field");
    List<String> names = invokeGetFieldNames(field);
    assertEquals(1, names.size());
    assertEquals("field", names.get(0));

    Field multiNameField = MultiNameClass.class.getDeclaredField("field");
    List<String> multiNames = invokeGetFieldNames(multiNameField);
    assertEquals(2, multiNames.size());
    assertTrue(multiNames.contains("field"));
    assertTrue(multiNames.contains("altName"));
  }

  @Test
    @Timeout(8000)
  void testCheckAccessible_setsAccessible() throws Exception {
    Method method = SampleClass.class.getDeclaredMethod("privateMethod");
    ReflectiveTypeAdapterFactory.checkAccessible(new Object(), method);
    assertTrue(method.canAccess(new SampleClass()));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_serializationDeserialization() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = SampleClass.class.getDeclaredField("field");
    Method accessor = SampleClass.class.getDeclaredMethod("getField");
    TypeToken<?> fieldType = TypeToken.get(String.class);

    // createBoundField is private, use reflection
    BoundField boundField = invokeCreateBoundField(factory, gson, field, accessor, "field", fieldType, true, true, true);
    assertNotNull(boundField);
    assertEquals("field", boundField.name);
    assertTrue(boundField.serialized);
    assertTrue(boundField.deserialized);
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_mapsFields() {
    Gson gson = mock(Gson.class);
    TypeToken<SampleClass> typeToken = TypeToken.get(SampleClass.class);

    // We do not block inaccessible in this test
    var boundFields = invokeGetBoundFields(factory, gson, typeToken, SampleClass.class, false, false);
    assertTrue(boundFields.containsKey("field"));
    assertTrue(boundFields.containsKey("transientField") == false);
  }

  // Helper to invoke private includeField
  private boolean invokeIncludeField(Field field, boolean serialize) {
    try {
      var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
      method.setAccessible(true);
      return (boolean) method.invoke(factory, field, serialize);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private getFieldNames
  private List<String> invokeGetFieldNames(Field field) {
    try {
      var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
      method.setAccessible(true);
      return (List<String>) method.invoke(factory, field);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private createBoundField
  private BoundField invokeCreateBoundField(ReflectiveTypeAdapterFactory factory, Gson gson, Field field, Method accessor,
      String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize, boolean blockInaccessible) {
    try {
      var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("createBoundField",
          Gson.class, Field.class, Method.class, String.class, TypeToken.class, boolean.class, boolean.class, boolean.class);
      method.setAccessible(true);
      return (BoundField) method.invoke(factory, gson, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Helper to invoke private getBoundFields
  private java.util.Map<String, BoundField> invokeGetBoundFields(ReflectiveTypeAdapterFactory factory, Gson gson,
      TypeToken<?> typeToken, Class<?> raw, boolean blockInaccessible, boolean isRecord) {
    try {
      var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getBoundFields",
          Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
      method.setAccessible(true);
      return (java.util.Map<String, BoundField>) method.invoke(factory, gson, typeToken, raw, blockInaccessible, isRecord);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static class SampleClass {
    String field = "value";
    transient String transientField = "transient";

    private String privateMethod() {
      return "private";
    }

    public String getField() {
      return field;
    }
  }

  static class MultiNameClass {
    @SerializedName(value = "field", alternate = {"altName"})
    String field;
  }
}