package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

class ReflectiveTypeAdapterFactory_79_2Test {

  private ReflectiveTypeAdapterFactory factory;
  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List reflectionFilters;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = new ArrayList<>();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder,
        jsonAdapterFactory, reflectionFilters);
  }

  static class TestClass {
    public int a;
    private String b;
    static int staticField;
    @SuppressWarnings("unused")
    private final int finalField = 1;
  }

  interface TestInterface {
    int x = 1;
  }

  static class SuperClass {
    public int superField;
  }

  static class SubClass extends SuperClass {
    public int subField;
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_interface_returnsEmptyMap() throws Exception {
    TypeToken<?> typeToken = TypeToken.get(TestInterface.class);
    Map<String, Object> result = invokeGetBoundFields(factory, mock(Gson.class), typeToken, TestInterface.class, false, false);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_classWithFields_includesFields() throws Exception {
    TypeToken<?> typeToken = TypeToken.get(TestClass.class);

    // Spy to control includeField behavior
    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), eq(true));
    doReturn(true).when(spyFactory).includeField(any(Field.class), eq(false));

    Map<String, Object> result = invokeGetBoundFields(spyFactory, mock(Gson.class), typeToken, TestClass.class, false, false);
    assertNotNull(result);
    assertTrue(result.containsKey("a"));
    assertTrue(result.containsKey("b"));
    assertFalse(result.containsKey("staticField")); // static fields included only if includeField returns true, but default excludes static
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_blockInaccessible_throwsOnBlockedSupertype() throws Exception {
    TypeToken<?> typeToken = TypeToken.get(SubClass.class);

    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());

    // Add a ReflectionAccessFilter that blocks all for SuperClass
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    when(filter.filter(SuperClass.class)).thenReturn(ReflectionAccessFilter.FilterResult.BLOCK_ALL);
    List filters = new ArrayList<>();
    filters.add(filter);

    ReflectiveTypeAdapterFactory factoryWithFilter = new ReflectiveTypeAdapterFactory(constructorConstructor,
        fieldNamingStrategy, excluder, jsonAdapterFactory, filters);

    JsonIOException exception = assertThrows(JsonIOException.class,
        () -> invokeGetBoundFields(factoryWithFilter, mock(Gson.class), typeToken, SubClass.class, false, false));
    assertTrue(exception.getMessage().contains("ReflectionAccessFilter does not permit using reflection"));
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_isRecord_accessorsUsed() throws Exception {
    class RecordLike {
      public int x;
      public int y;
    }
    TypeToken<?> typeToken = TypeToken.get(RecordLike.class);

    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());

    try (MockedStatic<ReflectionHelper> mockedReflectionHelper = mockStatic(ReflectionHelper.class)) {
      mockedReflectionHelper.when(() -> ReflectionHelper.getAccessor(any(), any())).thenAnswer(invocation -> {
        Field f = invocation.getArgument(1);
        Method m = mock(Method.class);
        when(m.getAnnotation(SerializedName.class)).thenReturn(null);
        when(m.getModifiers()).thenReturn(Modifier.PUBLIC);
        return m;
      });
      mockedReflectionHelper.when(() -> ReflectionHelper.makeAccessible(any())).then(invocation -> null);
      mockedReflectionHelper.when(() -> ReflectionHelper.getAccessibleObjectDescription(any(), anyBoolean())).thenReturn("desc");

      Map<String, Object> result = invokeGetBoundFields(spyFactory, mock(Gson.class), typeToken, RecordLike.class, false, true);
      assertNotNull(result);
      assertTrue(result.containsKey("x"));
      assertTrue(result.containsKey("y"));
    }
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_multipleFieldNames_conflictThrows() throws Exception {
    class ConflictingNames {
      @SerializedName("field")
      public int a;
      @SerializedName("field")
      public int b;
    }
    TypeToken<?> typeToken = TypeToken.get(ConflictingNames.class);

    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
    doReturn(Arrays.asList("field")).when(spyFactory).getFieldNames(any(Field.class));
    doReturn(mock(ReflectiveTypeAdapterFactory.BoundField.class)).when(spyFactory).createBoundField(any(), any(), any(), anyString(),
        any(), anyBoolean(), anyBoolean(), anyBoolean());

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> invokeGetBoundFields(spyFactory, mock(Gson.class), typeToken, ConflictingNames.class, false, false));
    assertTrue(ex.getMessage().contains("declares multiple JSON fields named 'field'"));
  }

  private Map<String, ReflectiveTypeAdapterFactory.BoundField> invokeGetBoundFields(ReflectiveTypeAdapterFactory factory,
                                                                                   Gson context,
                                                                                   TypeToken<?> type,
                                                                                   Class<?> raw,
                                                                                   boolean blockInaccessible,
                                                                                   boolean isRecord) throws Exception {
    java.lang.reflect.Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, ReflectiveTypeAdapterFactory.BoundField> result =
        (Map<String, ReflectiveTypeAdapterFactory.BoundField>) method.invoke(factory, context, type, raw, blockInaccessible, isRecord);
    return result;
  }
}