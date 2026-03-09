package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_getBoundFieldsTest {

  private ReflectiveTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder,
        jsonAdapterFactory, reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_interfaceReturnsEmptyMap() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<Object> type = TypeToken.get(Map.class);
    Class<?> raw = Map.class; // interface
    Map<String, Object> result = invokeGetBoundFields(gson, type, raw, false, false);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_classWithNoFieldsReturnsEmptyMap() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<Object> type = TypeToken.get(Object.class);
    Class<?> raw = Object.class; // no declared fields
    Map<String, Object> result = invokeGetBoundFields(gson, type, raw, false, false);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_blockAllThrowsJsonIOException() throws Exception {
    // Setup reflectionFilters to block all for a superclass
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter.FilterResult blockAll = FilterResult.BLOCK_ALL;
    List<ReflectionAccessFilter> filters = Collections.singletonList(filter);

    ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
    FieldNamingStrategy fieldNamingPolicy = mock(FieldNamingStrategy.class);
    Excluder excluder = mock(Excluder.class);
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);

    ReflectiveTypeAdapterFactory factoryWithFilters = new ReflectiveTypeAdapterFactory(
        constructorConstructor, fieldNamingPolicy, excluder, jsonAdapterFactory, filters);

    Gson gson = mock(Gson.class);
    TypeToken<ChildClass> type = TypeToken.get(ChildClass.class);
    Class<?> raw = ChildClass.class;

    try (MockedStatic<ReflectionAccessFilterHelper> mockStatic = Mockito.mockStatic(ReflectionAccessFilterHelper.class)) {
      mockStatic.when(() -> ReflectionAccessFilterHelper.getFilterResult(filters, ParentClass.class)).thenReturn(blockAll);

      JsonIOException ex = assertThrows(JsonIOException.class,
          () -> invokeGetBoundFields(factoryWithFilters, gson, type, raw, false, false));
      assertTrue(ex.getMessage().contains("ReflectionAccessFilter does not permit using reflection for"));
      assertTrue(ex.getMessage().contains("supertype of"));
    }
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_fieldsIncludedAndBoundFieldCreated() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<ClassWithFields> type = TypeToken.get(ClassWithFields.class);
    Class<?> raw = ClassWithFields.class;

    try (MockedStatic<ReflectionHelper> mockHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      mockHelper.when(() -> ReflectionHelper.makeAccessible(any(Field.class))).then(invocation -> null);
      mockHelper.when(() -> ReflectionHelper.getAccessor(any(), any(Field.class))).thenReturn(null);
      mockHelper.when(() -> ReflectionHelper.fieldToString(any(Field.class))).thenCallRealMethod();
      mockHelper.when(() -> ReflectionHelper.getAccessibleObjectDescription(any(Method.class), anyBoolean())).thenReturn("desc");

      ReflectiveTypeAdapterFactory spyFactory = spy(factory);
      doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
      doReturn(Collections.singletonList("field")).when(spyFactory).getFieldNames(any(Field.class));
      doReturn(mock(BoundField.class)).when(spyFactory).createBoundField(any(), any(), any(), anyString(),
          any(), anyBoolean(), anyBoolean(), anyBoolean());

      Map<String, BoundField> result = invokeGetBoundFields(spyFactory, gson, type, raw, false, false);
      assertNotNull(result);
      assertFalse(result.isEmpty());
      assertTrue(result.containsKey("field"));
    }
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_multipleFieldNameConflictThrows() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<ClassWithMultipleNamesConflict> type = TypeToken.get(ClassWithMultipleNamesConflict.class);
    Class<?> raw = ClassWithMultipleNamesConflict.class;

    try (MockedStatic<ReflectionHelper> mockHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      mockHelper.when(() -> ReflectionHelper.makeAccessible(any(Field.class))).then(invocation -> null);
      mockHelper.when(() -> ReflectionHelper.getAccessor(any(), any(Field.class))).thenReturn(null);
      mockHelper.when(() -> ReflectionHelper.fieldToString(any(Field.class))).thenReturn("fieldToString");
      mockHelper.when(() -> ReflectionHelper.getAccessibleObjectDescription(any(Method.class), anyBoolean())).thenReturn("desc");

      ReflectiveTypeAdapterFactory spyFactory = spy(factory);
      doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
      doReturn(List.of("name", "name")).when(spyFactory).getFieldNames(any(Field.class));
      doReturn(mock(BoundField.class)).when(spyFactory).createBoundField(any(), any(), any(), anyString(),
          any(), anyBoolean(), anyBoolean(), anyBoolean());

      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> invokeGetBoundFields(spyFactory, gson, type, raw, false, false));
      assertTrue(ex.getMessage().contains("declares multiple JSON fields named"));
    }
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_recordWithStaticFieldDeserializeFalse() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<RecordWithStaticField> type = TypeToken.get(RecordWithStaticField.class);
    Class<?> raw = RecordWithStaticField.class;

    try (MockedStatic<ReflectionHelper> mockHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      mockHelper.when(() -> ReflectionHelper.getAccessor(any(), any(Field.class))).thenReturn(mock(Method.class));
      mockHelper.when(() -> ReflectionHelper.makeAccessible(any())).then(invocation -> null);
      mockHelper.when(() -> ReflectionHelper.getAccessibleObjectDescription(any(Method.class), anyBoolean())).thenReturn("desc");

      ReflectiveTypeAdapterFactory spyFactory = spy(factory);
      doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
      doReturn(Collections.singletonList("staticField")).when(spyFactory).getFieldNames(any(Field.class));
      doReturn(mock(BoundField.class)).when(spyFactory).createBoundField(any(), any(), any(), anyString(),
          any(), anyBoolean(), anyBoolean(), anyBoolean());

      Map<String, BoundField> result = invokeGetBoundFields(spyFactory, gson, type, raw, false, true);
      assertNotNull(result);
      assertTrue(result.containsKey("staticField"));
    }
  }

  // Utility to invoke private getBoundFields method via reflection
  @SuppressWarnings("unchecked")
  private Map<String, BoundField> invokeGetBoundFields(Gson context, TypeToken<?> type, Class<?> raw,
      boolean blockInaccessible, boolean isRecord) throws Exception {
    return invokeGetBoundFields(factory, context, type, raw, blockInaccessible, isRecord);
  }

  @SuppressWarnings("unchecked")
  private Map<String, BoundField> invokeGetBoundFields(ReflectiveTypeAdapterFactory instance, Gson context, TypeToken<?> type,
      Class<?> raw, boolean blockInaccessible, boolean isRecord) throws Exception {
    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getBoundFields", Gson.class,
        TypeToken.class, Class.class, boolean.class, boolean.class);
    method.setAccessible(true);
    return (Map<String, BoundField>) method.invoke(instance, context, type, raw, blockInaccessible, isRecord);
  }

  // Helper classes for testing

  static class ParentClass {
    public int parentField;
  }

  static class ChildClass extends ParentClass {
    public int childField;
  }

  static class ClassWithFields {
    public int field;
  }

  static class ClassWithMultipleNamesConflict {
    public int field;
  }

  static class RecordWithStaticField {
    public static int staticField;
    public int instanceField;
  }
}