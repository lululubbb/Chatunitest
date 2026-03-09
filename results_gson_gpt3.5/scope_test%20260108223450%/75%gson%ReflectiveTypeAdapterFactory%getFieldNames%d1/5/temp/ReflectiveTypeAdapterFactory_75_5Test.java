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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectiveTypeAdapterFactory_75_5Test {

  private ReflectiveTypeAdapterFactory factory;
  private FieldNamingStrategy fieldNamingPolicy;

  static class TestClassNoAnnotation {
    public int someField;
  }

  static class TestClassWithSerializedName {
    @SerializedName("customName")
    public int annotatedField;
  }

  static class TestClassWithSerializedNameAndAlternates {
    @SerializedName(value = "primaryName", alternate = {"alt1", "alt2"})
    public int multiNamedField;
  }

  @BeforeEach
  void setUp() {
    fieldNamingPolicy = mock(FieldNamingStrategy.class);
    // Using null or mocks for other constructor params since getFieldNames does not use them
    factory = new ReflectiveTypeAdapterFactory(null, fieldNamingPolicy, null, null, Collections.emptyList());
  }

  @Test
    @Timeout(8000)
  void getFieldNames_noAnnotation_returnsFieldNamingPolicyName() throws Exception {
    Field field = TestClassNoAnnotation.class.getField("someField");
    when(fieldNamingPolicy.translateName(field)).thenReturn("translatedName");

    List<String> result = invokeGetFieldNames(field);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("translatedName", result.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedNameNoAlternates_returnsSerializedName() throws Exception {
    Field field = TestClassWithSerializedName.class.getField("annotatedField");

    List<String> result = invokeGetFieldNames(field);

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("customName", result.get(0));
  }

  @Test
    @Timeout(8000)
  void getFieldNames_withSerializedNameWithAlternates_returnsAllNames() throws Exception {
    Field field = TestClassWithSerializedNameAndAlternates.class.getField("multiNamedField");

    List<String> result = invokeGetFieldNames(field);

    assertNotNull(result);
    assertEquals(3, result.size());
    assertEquals("primaryName", result.get(0));
    assertEquals("alt1", result.get(1));
    assertEquals("alt2", result.get(2));
  }

  private List<String> invokeGetFieldNames(Field field) throws Exception {
    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<String> result = (List<String>) method.invoke(factory, field);
    return result;
  }
}