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

class ReflectiveTypeAdapterFactory_79_4Test {

  private ReflectiveTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingPolicy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;
  private Gson gson;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder,
        jsonAdapterFactory, reflectionFilters);
    gson = mock(Gson.class);
  }

  static class TestClass {
    public int field1;
    private String field2;
    @SuppressWarnings("unused")
    static int staticField = 10;
  }

  static class InterfaceClass {
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_Interface_ReturnsEmpty() throws Exception {
    TypeToken<?> typeToken = TypeToken.get(InterfaceClass.class);
    Map<String, Object> result = invokeGetBoundFields(factory, gson, typeToken, InterfaceClass.class, false, false);
    assertTrue(result.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_SimpleClass_FieldsReturned() throws Exception {
    TypeToken<?> typeToken = TypeToken.get(TestClass.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = invokeGetBoundFields(factory, gson, typeToken, TestClass.class, false, false);
    // Should include field1 and field2 (private)
    assertTrue(result.containsKey("field1"));
    assertTrue(result.containsKey("field2"));
    // Static field should not be included
    assertFalse(result.containsKey("staticField"));
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_BlockInaccessibleTrue_AccessibilityNotForced() throws Exception {
    // Use a subclass of ReflectiveTypeAdapterFactory to override includeField to true for all fields
    ReflectiveTypeAdapterFactory factorySpy = Mockito.spy(factory);
    doReturn(true).when(factorySpy).includeField(any(Field.class), anyBoolean());
    TypeToken<?> typeToken = TypeToken.get(TestClass.class);
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = invokeGetBoundFields(factorySpy, gson, typeToken, TestClass.class, true, false);
    // Should include fields but not call ReflectionHelper.makeAccessible on fields or accessors
    assertTrue(result.containsKey("field1"));
    assertTrue(result.containsKey("field2"));
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_SuperClassBlocked_ThrowsJsonIOException() throws Exception {
    // Prepare a reflection filter that blocks all for a superclass
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.filterResult(any())).thenReturn(FilterResult.BLOCK_ALL);
    List<ReflectionAccessFilter> filters = Collections.singletonList(filter);
    ReflectiveTypeAdapterFactory factoryWithFilter = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder,
        jsonAdapterFactory, filters);

    class SuperClass {
      public int superField;
    }
    class SubClass extends SuperClass {
      public int subField;
    }

    TypeToken<?> typeToken = TypeToken.get(SubClass.class);

    // Mock ReflectionAccessFilterHelper.getFilterResult to return BLOCK_ALL for SuperClass
    try (MockedStatic<com.google.gson.internal.ReflectionAccessFilterHelper> mockedStatic = Mockito.mockStatic(com.google.gson.internal.ReflectionAccessFilterHelper.class)) {
      mockedStatic.when(() -> com.google.gson.internal.ReflectionAccessFilterHelper.getFilterResult(filters, SuperClass.class))
          .thenReturn(FilterResult.BLOCK_ALL);
      mockedStatic.when(() -> com.google.gson.internal.ReflectionAccessFilterHelper.getFilterResult(filters, SubClass.class))
          .thenReturn(FilterResult.BLOCK_INACCESSIBLE);

      JsonIOException thrown = assertThrows(JsonIOException.class,
          () -> invokeGetBoundFields(factoryWithFilter, gson, typeToken, SubClass.class, false, false));
      assertTrue(thrown.getMessage().contains("ReflectionAccessFilter does not permit using reflection"));
    }
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_RecordWithAccessorAndSerializedNameOnAccessor_ThrowsJsonIOException() throws Exception {
    // Prepare a record-like class with a field and accessor method annotated with @SerializedName
    class RecordLike {
      public int value;
      public int value() { return value; }
    }
    // Spy ReflectionHelper.getAccessor to return a Method with @SerializedName annotation
    Method accessor = RecordLike.class.getMethod("value");
    Method accessorSpy = Mockito.spy(accessor);
    SerializedName serializedNameAnnotation = accessorSpy.getAnnotation(SerializedName.class);
    // We cannot add annotation at runtime, so mock ReflectionHelper.getAccessor to return a Method with SerializedName present
    try (MockedStatic<ReflectionHelper> mockedReflectionHelper = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedReflectionHelper.when(() -> ReflectionHelper.getAccessor(any(), any())).thenReturn(accessorSpy);
      mockedReflectionHelper.when(() -> ReflectionHelper.makeAccessible(any())).thenAnswer(i -> null);
      mockedReflectionHelper.when(() -> ReflectionHelper.getAccessibleObjectDescription(any(), anyBoolean())).thenReturn("accessorMethod");

      // Spy includeField to always return true
      ReflectiveTypeAdapterFactory factorySpy = Mockito.spy(factory);
      doReturn(true).when(factorySpy).includeField(any(Field.class), anyBoolean());
      TypeToken<?> typeToken = TypeToken.get(RecordLike.class);

      // Use reflection to set isRecord to true to trigger accessor logic
      JsonIOException thrown = assertThrows(JsonIOException.class,
          () -> invokeGetBoundFields(factorySpy, gson, typeToken, RecordLike.class, false, true));
      assertTrue(thrown.getMessage().contains("@SerializedName on accessorMethod is not supported"));
    }
  }

  private static <T> Map<String, ReflectiveTypeAdapterFactory.BoundField> invokeGetBoundFields(
      ReflectiveTypeAdapterFactory factory, Gson gson, TypeToken<T> type, Class<?> raw,
      boolean blockInaccessible, boolean isRecord) throws Exception {
    java.lang.reflect.Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result = (Map<String, ReflectiveTypeAdapterFactory.BoundField>) method.invoke(factory, gson, type, raw, blockInaccessible, isRecord);
    return result;
  }
}