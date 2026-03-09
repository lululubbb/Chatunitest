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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.google.gson.JsonIOException;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ReflectiveTypeAdapterFactory_77_1Test {

  // We cannot instantiate Field directly, so we use a real field for testing
  static class DummyClass {
    public static int staticField;
    public int instanceField;
  }

  private static Method checkAccessibleMethod;

  @BeforeAll
  static void setup() throws Exception {
    checkAccessibleMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
    checkAccessibleMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void checkAccessible_permitsAccessibleMember() throws Exception {
    Field instanceField = DummyClass.class.getDeclaredField("instanceField");
    Object object = new DummyClass();

    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> helperMock = mockStatic(ReflectionHelper.class)) {
      filterHelperMock.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, object)).thenReturn(true);

      checkAccessibleMethod.invoke(null, object, instanceField);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_permitsAccessibleStaticMember() throws Exception {
    Field staticField = DummyClass.class.getDeclaredField("staticField");

    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> helperMock = mockStatic(ReflectionHelper.class)) {
      filterHelperMock.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(true);

      checkAccessibleMethod.invoke(null, null, staticField);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOException_whenAccessDenied() throws Exception {
    Field instanceField = DummyClass.class.getDeclaredField("instanceField");
    Object object = new DummyClass();

    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> helperMock = mockStatic(ReflectionHelper.class)) {
      filterHelperMock.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, object)).thenReturn(false);
      helperMock.when(() -> ReflectionHelper.getAccessibleObjectDescription(instanceField, true))
          .thenReturn("field com.google.gson.internal.bind.ReflectiveTypeAdapterFactoryTest$DummyClass.instanceField");

      Exception exception = assertThrows(JsonIOException.class, () -> {
        try {
          checkAccessibleMethod.invoke(null, object, instanceField);
        } catch (Exception e) {
          // unwrap InvocationTargetException
          Throwable cause = e.getCause();
          if (cause instanceof JsonIOException) {
            throw (JsonIOException) cause;
          }
          throw e;
        }
      });
      String message = exception.getMessage();
      assert message.contains("field com.google.gson.internal.bind.ReflectiveTypeAdapterFactoryTest$DummyClass.instanceField");
      assert message.contains("is not accessible");
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOException_whenAccessDenied_staticField() throws Exception {
    Field staticField = DummyClass.class.getDeclaredField("staticField");

    try (MockedStatic<ReflectionAccessFilterHelper> filterHelperMock = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> helperMock = mockStatic(ReflectionHelper.class)) {
      filterHelperMock.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(false);
      helperMock.when(() -> ReflectionHelper.getAccessibleObjectDescription(staticField, true))
          .thenReturn("field com.google.gson.internal.bind.ReflectiveTypeAdapterFactoryTest$DummyClass.staticField");

      Exception exception = assertThrows(JsonIOException.class, () -> {
        try {
          checkAccessibleMethod.invoke(null, null, staticField);
        } catch (Exception e) {
          // unwrap InvocationTargetException
          Throwable cause = e.getCause();
          if (cause instanceof JsonIOException) {
            throw (JsonIOException) cause;
          }
          throw e;
        }
      });
      String message = exception.getMessage();
      assert message.contains("field com.google.gson.internal.bind.ReflectiveTypeAdapterFactoryTest$DummyClass.staticField");
      assert message.contains("is not accessible");
    }
  }
}