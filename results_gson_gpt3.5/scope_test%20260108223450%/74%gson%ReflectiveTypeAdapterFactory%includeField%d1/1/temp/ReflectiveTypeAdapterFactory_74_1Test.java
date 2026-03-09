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
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
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

import com.google.gson.internal.Excluder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class ReflectiveTypeAdapterFactory_74_1Test {

  private ReflectiveTypeAdapterFactory factory;

  @Mock
  private Excluder excluder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new ReflectiveTypeAdapterFactory(
        null, // ConstructorConstructor not used by includeField
        null, // FieldNamingStrategy not used by includeField
        excluder,
        null, // JsonAdapterAnnotationTypeAdapterFactory not used by includeField
        null  // reflectionFilters not used by includeField
    );
  }

  private boolean invokeIncludeField(Field f, boolean serialize) throws Exception {
    var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
    method.setAccessible(true);
    return (boolean) method.invoke(factory, f, serialize);
  }

  @Test
    @Timeout(8000)
  void includeField_excludeClassTrue() throws Exception {
    Field field = SampleClass.class.getDeclaredField("value");
    when(excluder.excludeClass(field.getType(), true)).thenReturn(true);
    when(excluder.excludeField(field, true)).thenReturn(false);

    boolean result = invokeIncludeField(field, true);

    assertFalse(result);
    verify(excluder).excludeClass(field.getType(), true);
    verify(excluder, never()).excludeField(field, true);
  }

  @Test
    @Timeout(8000)
  void includeField_excludeFieldTrue() throws Exception {
    Field field = SampleClass.class.getDeclaredField("value");
    when(excluder.excludeClass(field.getType(), false)).thenReturn(false);
    when(excluder.excludeField(field, false)).thenReturn(true);

    boolean result = invokeIncludeField(field, false);

    assertFalse(result);
    verify(excluder).excludeClass(field.getType(), false);
    verify(excluder).excludeField(field, false);
  }

  @Test
    @Timeout(8000)
  void includeField_includeField() throws Exception {
    Field field = SampleClass.class.getDeclaredField("value");
    when(excluder.excludeClass(field.getType(), true)).thenReturn(false);
    when(excluder.excludeField(field, true)).thenReturn(false);

    boolean result = invokeIncludeField(field, true);

    assertTrue(result);
    verify(excluder).excludeClass(field.getType(), true);
    verify(excluder).excludeField(field, true);
  }

  private static class SampleClass {
    int value;
  }
}