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

import java.lang.reflect.InvocationTargetException;

class ReflectiveTypeAdapterFactoryCheckAccessibleTest {

  private Object testObject;

  private Field staticField;
  private Field instanceField;
  private Method instanceMethod;

  @BeforeEach
  void setUp() throws NoSuchFieldException, NoSuchMethodException {
    testObject = new Object();

    staticField = StaticTestClass.class.getDeclaredField("STATIC_FIELD");
    instanceField = StaticTestClass.class.getDeclaredField("instanceField");
    instanceMethod = StaticTestClass.class.getDeclaredMethod("instanceMethod");
  }

  // Helper class with static and instance members for testing
  static class StaticTestClass {
    static final String STATIC_FIELD = "static";
    private int instanceField = 1;
    private void instanceMethod() {}
  }

  private static void invokeCheckAccessible(Object object, AccessibleObject member) throws Exception {
    Method method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
    method.setAccessible(true);
    method.invoke(null, object, member);
  }

  @Test
    @Timeout(8000)
  void checkAccessible_allowsAccess_whenCanAccessReturnsTrue_forStaticField() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(true);

      // Should not throw
      invokeCheckAccessible(null, staticField);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_allowsAccess_whenCanAccessReturnsTrue_forInstanceField() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, testObject)).thenReturn(true);

      // Should not throw
      invokeCheckAccessible(testObject, instanceField);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOException_whenCanAccessReturnsFalse_forStaticField() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(false);
      String desc = "Static field description";
      mockedHelperDesc.when(() -> ReflectionHelper.getAccessibleObjectDescription(staticField, true)).thenReturn(desc);

      InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
        invokeCheckAccessible(null, staticField);
      });
      Throwable cause = ex.getCause();
      assertTrue(cause instanceof JsonIOException);
      assertTrue(cause.getMessage().contains(desc));
      assertTrue(cause.getMessage().contains("is not accessible"));
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOException_whenCanAccessReturnsFalse_forInstanceField() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, testObject)).thenReturn(false);
      String desc = "Instance field description";
      mockedHelperDesc.when(() -> ReflectionHelper.getAccessibleObjectDescription(instanceField, true)).thenReturn(desc);

      InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
        invokeCheckAccessible(testObject, instanceField);
      });
      Throwable cause = ex.getCause();
      assertTrue(cause instanceof JsonIOException);
      assertTrue(cause.getMessage().contains(desc));
      assertTrue(cause.getMessage().contains("is not accessible"));
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOException_whenCanAccessReturnsFalse_forInstanceMethod() throws Exception {
    try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = Mockito.mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> mockedHelperDesc = Mockito.mockStatic(ReflectionHelper.class)) {
      mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceMethod, testObject)).thenReturn(false);
      String desc = "Instance method description";
      mockedHelperDesc.when(() -> ReflectionHelper.getAccessibleObjectDescription(instanceMethod, true)).thenReturn(desc);

      InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
        invokeCheckAccessible(testObject, instanceMethod);
      });
      Throwable cause = ex.getCause();
      assertTrue(cause instanceof JsonIOException);
      assertTrue(cause.getMessage().contains(desc));
      assertTrue(cause.getMessage().contains("is not accessible"));
    }
  }
}