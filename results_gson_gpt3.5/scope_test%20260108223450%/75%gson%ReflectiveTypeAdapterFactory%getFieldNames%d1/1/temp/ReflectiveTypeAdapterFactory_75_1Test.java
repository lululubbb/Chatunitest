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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.annotations.SerializedName;
import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReflectiveTypeAdapterFactory_75_1Test {

  private ReflectiveTypeAdapterFactory factory;

  @Mock
  private FieldNamingStrategy fieldNamingStrategy;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    // Provide mocks or nulls for other constructor params not used in getFieldNames
    factory = new ReflectiveTypeAdapterFactory(
        null,
        fieldNamingStrategy,
        null,
        null,
        Collections.emptyList()
    );
  }

  @Test
    @Timeout(8000)
  void getFieldNames_noSerializedNameAnnotation_returnsTranslatedName() throws Exception {
    Field field = TestClass.class.getDeclaredField("fieldWithoutAnnotation");
    String translatedName = "translatedFieldName";
    when(fieldNamingStrategy.translateName(field)).thenReturn(translatedName);

    List<String> result = invokeGetFieldNames(field);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(translatedName, result.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedName_noAlternates_returnsSerializedName() throws Exception {
    Field field = TestClass.class.getDeclaredField("fieldWithSerializedNameNoAlternates");

    List<String> result = invokeGetFieldNames(field);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("primaryName", result.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedName_withAlternates_returnsSerializedNameAndAlternates() throws Exception {
    Field field = TestClass.class.getDeclaredField("fieldWithSerializedNameWithAlternates");

    List<String> result = invokeGetFieldNames(field);

    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("primaryName", result.get(0));
    assertTrue(result.containsAll(Arrays.asList("alternate1", "alternate2")));
  }

  // Utility method to invoke private getFieldNames via reflection
  @SuppressWarnings("unchecked")
  private List<String> invokeGetFieldNames(Field field) throws Exception {
    java.lang.reflect.Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    method.setAccessible(true);
    return (List<String>) method.invoke(factory, field);
  }

  // Test class with fields to cover different SerializedName annotation cases
  private static class TestClass {
    String fieldWithoutAnnotation;

    @SerializedName("primaryName")
    String fieldWithSerializedNameNoAlternates;

    @SerializedName(value = "primaryName", alternate = {"alternate1", "alternate2"})
    String fieldWithSerializedNameWithAlternates;
  }
}