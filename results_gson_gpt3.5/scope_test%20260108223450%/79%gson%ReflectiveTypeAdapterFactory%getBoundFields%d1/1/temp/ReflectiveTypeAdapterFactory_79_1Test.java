package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
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
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField;
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

public class ReflectiveTypeAdapterFactory_79_1Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<com.google.gson.ReflectionAccessFilter> reflectionFilters;
  private ReflectiveTypeAdapterFactory factory;
  private Gson gson;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();
    factory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, reflectionFilters);
    gson = mock(Gson.class);
  }

  static class TestClass {
    public int publicField = 1;
    private String privateField = "private";
    @SuppressWarnings("unused")
    static int staticField = 100;
  }

  static class TestRecord {
    public final int x;
    public final String y;

    public TestRecord(int x, String y) {
      this.x = x;
      this.y = y;
    }
  }

  @Test
    @Timeout(8000)
  public void testGetBoundFields_InterfaceReturnsEmpty() throws Exception {
    Class<?> iface = Runnable.class;
    TypeToken<?> typeToken = TypeToken.get(iface);

    Map<String, BoundField> result = invokeGetBoundFields(gson, typeToken, iface, false, false);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testGetBoundFields_IncludesPublicAndPrivateFields() throws Exception {
    Class<?> clazz = TestClass.class;
    TypeToken<?> typeToken = TypeToken.get(clazz);

    // Spy on factory to allow includeField to return true for all fields
    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
    doReturn(Collections.singletonList("publicField")).when(spyFactory).getFieldNames(any(Field.class));
    doReturn(Collections.singletonList("privateField")).when(spyFactory).getFieldNames(any(Field.class));

    Map<String, BoundField> result = invokeGetBoundFields(gson, typeToken, clazz, false, false, spyFactory);

    assertNotNull(result);
    // Because getFieldNames is stubbed to single name, result keys should contain those names
    assertTrue(result.containsKey("publicField") || result.containsKey("privateField"));
  }

  @Test
    @Timeout(8000)
  public void testGetBoundFields_BlocksInaccessibleThrowsJsonIOException() throws Exception {
    Class<?> superClass = SuperBlocked.class;
    TypeToken<?> typeToken = TypeToken.get(SubBlocked.class);

    // Create a reflection filter that blocks all access to SuperBlocked
    com.google.gson.ReflectionAccessFilter filter = new com.google.gson.ReflectionAccessFilter() {
      @Override
      public com.google.gson.ReflectionAccessFilter.FilterResult checkAccess(Class<?> clazz) {
        if (clazz == SuperBlocked.class) {
          return com.google.gson.ReflectionAccessFilter.FilterResult.BLOCK_ALL;
        }
        return com.google.gson.ReflectionAccessFilter.FilterResult.ALLOW;
      }
    };
    List<com.google.gson.ReflectionAccessFilter> filters = Collections.singletonList(filter);
    ReflectiveTypeAdapterFactory factoryWithFilter = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder, jsonAdapterFactory, filters);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> {
      invokeGetBoundFields(gson, typeToken, SubBlocked.class, false, false, factoryWithFilter);
    });
    assertTrue(ex.getMessage().contains("ReflectionAccessFilter does not permit using reflection"));
  }

  static class SuperBlocked {
    public int superField;
  }

  static class SubBlocked extends SuperBlocked {
    public int subField;
  }

  @Test
    @Timeout(8000)
  public void testGetBoundFields_RecordWithAccessor() throws Exception {
    Class<?> recordClass = TestRecord.class;
    TypeToken<?> typeToken = TypeToken.get(recordClass);

    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
    doReturn(Collections.singletonList("x")).when(spyFactory).getFieldNames(any(Field.class));
    doReturn(Collections.singletonList("y")).when(spyFactory).getFieldNames(any(Field.class));

    try (MockedStatic<ReflectionHelper> reflectionHelperMockedStatic = Mockito.mockStatic(ReflectionHelper.class)) {
      reflectionHelperMockedStatic.when(() -> ReflectionHelper.getAccessor(eq(recordClass), any(Field.class)))
          .thenAnswer(invocation -> {
            Field field = invocation.getArgument(1);
            // Return a dummy Method object for accessor
            Method method = TestRecord.class.getMethod(field.getName());
            return method;
          });
      reflectionHelperMockedStatic.when(() -> ReflectionHelper.makeAccessible(any())).then(invocation -> null);
      reflectionHelperMockedStatic.when(() -> ReflectionHelper.fieldToString(any(Field.class))).thenCallRealMethod();
      reflectionHelperMockedStatic.when(() -> ReflectionHelper.getAccessibleObjectDescription(any(), anyBoolean())).thenCallRealMethod();

      Map<String, BoundField> result = invokeGetBoundFields(gson, typeToken, recordClass, false, true, spyFactory);

      assertNotNull(result);
      assertTrue(result.containsKey("x"));
      assertTrue(result.containsKey("y"));
    }
  }

  @Test
    @Timeout(8000)
  public void testGetBoundFields_FieldNameConflictThrows() throws Exception {
    class ConflictClass {
      @SuppressWarnings("unused")
      public int a;
      @SuppressWarnings("unused")
      public int a;
    }

    // We cannot declare two fields with the same name in Java, so simulate by mocking getFieldNames to return duplicates

    Class<?> clazz = TestClass.class;
    TypeToken<?> typeToken = TypeToken.get(clazz);

    ReflectiveTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(true).when(spyFactory).includeField(any(Field.class), anyBoolean());
    doReturn(Arrays.asList("conflict", "conflict")).when(spyFactory).getFieldNames(any(Field.class));
    doReturn(mock(BoundField.class)).when(spyFactory).createBoundField(any(), any(), any(), anyString(), any(), anyBoolean(), anyBoolean(), anyBoolean());

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      invokeGetBoundFields(gson, typeToken, clazz, false, false, spyFactory);
    });
    assertTrue(thrown.getMessage().contains("declares multiple JSON fields named"));
  }

  private Map<String, BoundField> invokeGetBoundFields(Gson gson, TypeToken<?> type, Class<?> raw,
                                                      boolean blockInaccessible, boolean isRecord) throws Exception {
    return invokeGetBoundFields(gson, type, raw, blockInaccessible, isRecord, factory);
  }

  private Map<String, BoundField> invokeGetBoundFields(Gson gson, TypeToken<?> type, Class<?> raw,
                                                      boolean blockInaccessible, boolean isRecord,
                                                      ReflectiveTypeAdapterFactory factoryInstance) throws Exception {
    java.lang.reflect.Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, BoundField> result = (Map<String, BoundField>) method.invoke(factoryInstance, gson, type, raw, blockInaccessible, isRecord);
    return result;
  }
}