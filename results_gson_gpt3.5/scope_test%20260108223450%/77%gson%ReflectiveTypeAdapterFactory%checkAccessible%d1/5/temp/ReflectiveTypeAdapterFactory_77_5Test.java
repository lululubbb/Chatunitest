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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ReflectiveTypeAdapterFactory_77_5Test {

  private static class TestMember extends AccessibleObject implements Member {
    private final boolean isStatic;
    private boolean accessible;

    TestMember(boolean isStatic) {
      this.isStatic = isStatic;
      this.accessible = false;
    }

    @Override
    public Class<?> getDeclaringClass() {
      return this.getClass();
    }

    @Override
    public String getName() {
      return "testMember";
    }

    @Override
    public int getModifiers() {
      return isStatic ? Modifier.STATIC : 0;
    }

    @Override
    public boolean isSynthetic() {
      return false;
    }

    @Override
    public void setAccessible(boolean flag) {
      accessible = flag;
    }

    @Override
    public boolean isAccessible() {
      return accessible;
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_permitsAccessWhenFilterAllows() throws Exception {
    TestMember member = new TestMember(false);

    try (MockedStatic<ReflectionAccessFilterHelper> rafMocked = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> rhMocked = mockStatic(ReflectionHelper.class)) {

      rafMocked.when(() -> ReflectionAccessFilterHelper.canAccess(member, member))
          .thenReturn(true);

      Method method = ReflectiveTypeAdapterFactory.class
          .getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
      method.setAccessible(true);

      // Should not throw
      method.invoke(null, member, member);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOExceptionWhenFilterDenies() throws Exception {
    TestMember member = new TestMember(false);

    try (MockedStatic<ReflectionAccessFilterHelper> rafMocked = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> rhMocked = mockStatic(ReflectionHelper.class)) {

      rafMocked.when(() -> ReflectionAccessFilterHelper.canAccess(member, member))
          .thenReturn(false);
      rhMocked.when(() -> ReflectionHelper.getAccessibleObjectDescription(member, true))
          .thenReturn("TestMember description");

      Method method = ReflectiveTypeAdapterFactory.class
          .getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
      method.setAccessible(true);

      Exception ex = assertThrows(JsonIOException.class, () -> {
        try {
          method.invoke(null, member, member);
        } catch (Exception e) {
          // Unwrap InvocationTargetException
          Throwable cause = e.getCause();
          if (cause instanceof JsonIOException) {
            throw (JsonIOException) cause;
          }
          throw e;
        }
      });

      String message = ex.getMessage();
      assert message.contains("TestMember description is not accessible");
      assert message.contains("ReflectionAccessFilter does not permit");
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_passesNullObjectForStaticMember() throws Exception {
    TestMember member = new TestMember(true);

    try (MockedStatic<ReflectionAccessFilterHelper> rafMocked = mockStatic(ReflectionAccessFilterHelper.class);
         MockedStatic<ReflectionHelper> rhMocked = mockStatic(ReflectionHelper.class)) {

      rafMocked.when(() -> ReflectionAccessFilterHelper.canAccess(member, null))
          .thenReturn(true);

      Method method = ReflectiveTypeAdapterFactory.class
          .getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
      method.setAccessible(true);

      // Should not throw
      method.invoke(null, null, member);
    }
  }
}