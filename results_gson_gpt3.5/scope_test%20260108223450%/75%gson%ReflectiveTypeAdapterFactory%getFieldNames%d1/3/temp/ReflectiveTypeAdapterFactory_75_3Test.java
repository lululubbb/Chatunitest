package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
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

import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReflectiveTypeAdapterFactory_75_3Test {

  ReflectiveTypeAdapterFactory factory;

  @Mock
  ConstructorConstructor constructorConstructorMock;
  @Mock
  FieldNamingStrategy fieldNamingStrategyMock;
  @Mock
  Excluder excluderMock;
  @Mock
  JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactoryMock;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new ReflectiveTypeAdapterFactory(
        constructorConstructorMock,
        fieldNamingStrategyMock,
        excluderMock,
        jsonAdapterFactoryMock,
        Collections.emptyList()
    );
  }

  private List<String> invokeGetFieldNames(Field field) throws Exception {
    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<String> result = (List<String>) method.invoke(factory, field);
    return result;
  }

  static class NoAnnotationClass {
    String someField;
  }

  static class SerializedNameNoAlternate {
    @SerializedName("customName")
    String field;
  }

  static class SerializedNameWithAlternate {
    @SerializedName(value = "primaryName", alternate = {"alt1", "alt2"})
    String field;
  }

  @Test
    @Timeout(8000)
  void getFieldNames_noSerializedNameAnnotation_returnsFieldNamingPolicyName() throws Exception {
    Field field = NoAnnotationClass.class.getDeclaredField("someField");
    when(fieldNamingStrategyMock.translateName(field)).thenReturn("translatedName");

    List<String> names = invokeGetFieldNames(field);

    assertNotNull(names);
    assertEquals(1, names.size());
    assertEquals("translatedName", names.get(0));
    verify(fieldNamingStrategyMock).translateName(field);
  }

  @Test
    @Timeout(8000)
  void getFieldNames_serializedNameWithoutAlternates_returnsSingleName() throws Exception {
    Field field = SerializedNameNoAlternate.class.getDeclaredField("field");

    List<String> names = invokeGetFieldNames(field);

    assertNotNull(names);
    assertEquals(1, names.size());
    assertEquals("customName", names.get(0));
    verifyNoInteractions(fieldNamingStrategyMock);
  }

  @Test
    @Timeout(8000)
  void getFieldNames_serializedNameWithAlternates_returnsAllNames() throws Exception {
    Field field = SerializedNameWithAlternate.class.getDeclaredField("field");

    List<String> names = invokeGetFieldNames(field);

    assertNotNull(names);
    assertEquals(3, names.size());
    assertEquals("primaryName", names.get(0));
    assertTrue(names.contains("alt1"));
    assertTrue(names.contains("alt2"));
    verifyNoInteractions(fieldNamingStrategyMock);
  }
}