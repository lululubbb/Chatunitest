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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.FieldNamingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.List;

class ReflectiveTypeAdapterFactory_75_4Test {

  @Mock
  private ConstructorConstructor mockConstructorConstructor;
  @Mock
  private FieldNamingStrategy mockFieldNamingStrategy;
  @Mock
  private Excluder mockExcluder;
  @Mock
  private JsonAdapterAnnotationTypeAdapterFactory mockJsonAdapterFactory;
  @Mock
  private List<ReflectionAccessFilter> mockReflectionFilters;

  private ReflectiveTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new ReflectiveTypeAdapterFactory(
        mockConstructorConstructor,
        mockFieldNamingStrategy,
        mockExcluder,
        mockJsonAdapterFactory,
        mockReflectionFilters);
  }

  private List<String> invokeGetFieldNames(Field field) throws Exception {
    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    method.setAccessible(true);
    //noinspection unchecked
    return (List<String>) method.invoke(factory, field);
  }

  static class NoSerializedName {
    int someField;
  }

  static class WithSerializedName {
    @SerializedName("customName")
    int field;
  }

  static class WithSerializedNameAlternates {
    @SerializedName(value = "mainName", alternate = {"alt1", "alt2"})
    int field;
  }

  @Test
    @Timeout(8000)
  void getFieldNames_noAnnotation_returnsTranslatedName() throws Exception {
    Field field = NoSerializedName.class.getDeclaredField("someField");
    when(mockFieldNamingStrategy.translateName(field)).thenReturn("translatedName");

    List<String> names = invokeGetFieldNames(field);

    assertNotNull(names);
    assertEquals(1, names.size());
    assertEquals("translatedName", names.get(0));
    verify(mockFieldNamingStrategy).translateName(field);
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedName_noAlternates_returnsSingleSerializedName() throws Exception {
    Field field = WithSerializedName.class.getDeclaredField("field");

    List<String> names = invokeGetFieldNames(field);

    assertNotNull(names);
    assertEquals(1, names.size());
    assertEquals("customName", names.get(0));
    verifyNoInteractions(mockFieldNamingStrategy);
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedName_withAlternates_returnsAllNames() throws Exception {
    Field field = WithSerializedNameAlternates.class.getDeclaredField("field");

    List<String> names = invokeGetFieldNames(field);

    assertNotNull(names);
    assertEquals(3, names.size());
    assertEquals("mainName", names.get(0));
    assertTrue(names.contains("alt1"));
    assertTrue(names.contains("alt2"));
    verifyNoInteractions(mockFieldNamingStrategy);
  }
}