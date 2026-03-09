package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
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
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
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

import com.google.gson.JsonIOException;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectiveTypeAdapterFactory_77_6Test {

  private Object instance;

  @BeforeEach
  void setUp() {
    instance = new Object();
  }

  @Test
    @Timeout(8000)
  void checkAccessible_staticMember_accessAllowed() throws Exception {
    // Arrange
    Field staticField = SampleClass.class.getDeclaredField("STATIC_FIELD");
    Method checkAccessible = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
    checkAccessible.setAccessible(true);

    // static field, so object param is null
    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(true);

      // Act & Assert no exception
      checkAccessible.invoke(null, null, staticField);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_instanceMember_accessAllowed() throws Exception {
    // Arrange
    Field instanceField = SampleClass.class.getDeclaredField("instanceField");
    Method checkAccessible = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
    checkAccessible.setAccessible(true);

    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, instance)).thenReturn(true);

      // Act & Assert no exception
      checkAccessible.invoke(null, instance, instanceField);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_staticMember_accessDenied_throws() throws Exception {
    // Arrange
    Field staticField = SampleClass.class.getDeclaredField("STATIC_FIELD");
    Method checkAccessible = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
    checkAccessible.setAccessible(true);

    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(false);
      mockedHelperDesc.when(() -> ReflectionHelper.getAccessibleObjectDescription(staticField, true))
          .thenReturn("Static Field Description");

      // Act & Assert exception with expected message
      Exception ex = assertThrows(Exception.class, () -> {
        checkAccessible.invoke(null, null, staticField);
      });
      // InvocationTargetException wraps the JsonIOException, unwrap it:
      Throwable cause = ex.getCause();
      assertTrue(cause instanceof JsonIOException);
      String msg = cause.getMessage();
      assertTrue(msg.contains("Static Field Description is not accessible"));
      assertTrue(msg.contains("ReflectionAccessFilter does not permit making it accessible"));
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_instanceMember_accessDenied_throws() throws Exception {
    // Arrange
    Field instanceField = SampleClass.class.getDeclaredField("instanceField");
    Method checkAccessible = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
    checkAccessible.setAccessible(true);

    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, instance)).thenReturn(false);
      mockedHelperDesc.when(() -> ReflectionHelper.getAccessibleObjectDescription(instanceField, true))
          .thenReturn("Instance Field Description");

      // Act & Assert exception with expected message
      Exception ex = assertThrows(Exception.class, () -> {
        checkAccessible.invoke(null, instance, instanceField);
      });
      Throwable cause = ex.getCause();
      assertTrue(cause instanceof JsonIOException);
      String msg = cause.getMessage();
      assertTrue(msg.contains("Instance Field Description is not accessible"));
      assertTrue(msg.contains("ReflectionAccessFilter does not permit making it accessible"));
    }
  }

  // Helper class for testing
  static class SampleClass {
    static int STATIC_FIELD;
    int instanceField;
  }
}