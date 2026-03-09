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
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
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
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ReflectiveTypeAdapterFactory_77_2Test {

  private static class TestMember extends AccessibleObject implements Member {
    private final int modifiers;
    private final boolean isStatic;

    TestMember(int modifiers, boolean isStatic) {
      this.modifiers = modifiers;
      this.isStatic = isStatic;
    }

    @Override
    public Class<?> getDeclaringClass() {
      return TestMember.class;
    }

    @Override
    public String getName() {
      return "testMember";
    }

    @Override
    public int getModifiers() {
      return modifiers;
    }

    @Override
    public boolean isSynthetic() {
      return false;
    }

    @Override
    public void setAccessible(boolean flag) {
      // no-op
    }

    @Override
    public boolean isAccessible() {
      return true;
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_allowsAccessibleMember() throws Exception {
    Method member = TestMember.class.getDeclaredMethod("getName");
    Object object = new Object();

    try (MockedStatic<ReflectionAccessFilterHelper> mocked = mockStatic(ReflectionAccessFilterHelper.class)) {
      mocked.when(() -> ReflectionAccessFilterHelper.canAccess(member, object)).thenReturn(true);

      // invoke private static method via reflection
      Method checkAccessibleMethod = ReflectiveTypeAdapterFactory.class
          .getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
      checkAccessibleMethod.setAccessible(true);
      // should not throw
      checkAccessibleMethod.invoke(null, object, member);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_allowsAccessibleStaticMember() throws Exception {
    Method member = TestMember.class.getDeclaredMethod("getName");
    Object object = new Object();

    // Create a subclass of AccessibleObject that implements Member for static member
    class StaticAccessibleMember extends AccessibleObject implements Member {
      @Override
      public Class<?> getDeclaringClass() {
        return TestMember.class;
      }

      @Override
      public String getName() {
        return "staticMember";
      }

      @Override
      public int getModifiers() {
        return Modifier.STATIC;
      }

      @Override
      public boolean isSynthetic() {
        return false;
      }

      @Override
      public boolean isAccessible() {
        return true;
      }

      @Override
      public void setAccessible(boolean flag) {
        // no-op
      }
    }

    StaticAccessibleMember staticAccessibleMemberInstance = new StaticAccessibleMember();

    try (MockedStatic<ReflectionAccessFilterHelper> mocked = mockStatic(ReflectionAccessFilterHelper.class)) {
      // For static member, object param is null
      mocked.when(() -> ReflectionAccessFilterHelper.canAccess(staticAccessibleMemberInstance, null)).thenReturn(true);

      Method checkAccessibleMethod = ReflectiveTypeAdapterFactory.class
          .getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
      checkAccessibleMethod.setAccessible(true);
      // should not throw
      checkAccessibleMethod.invoke(null, object, staticAccessibleMemberInstance);
    }
  }

  @Test
    @Timeout(8000)
  void checkAccessible_throwsJsonIOException_whenNotAccessible() throws Exception {
    Field field = TestMember.class.getDeclaredField("modifiers");
    Object object = new Object();

    try (MockedStatic<ReflectionAccessFilterHelper> mocked = mockStatic(ReflectionAccessFilterHelper.class);
        MockedStatic<com.google.gson.internal.reflect.ReflectionHelper> mockedHelper = mockStatic(com.google.gson.internal.reflect.ReflectionHelper.class)) {
      mocked.when(() -> ReflectionAccessFilterHelper.canAccess(field, object)).thenReturn(false);
      mockedHelper.when(() -> com.google.gson.internal.reflect.ReflectionHelper.getAccessibleObjectDescription(field, true))
          .thenReturn("field description");

      Method checkAccessibleMethod = ReflectiveTypeAdapterFactory.class
          .getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
      checkAccessibleMethod.setAccessible(true);

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        try {
          checkAccessibleMethod.invoke(null, object, field);
        } catch (InvocationTargetException e) {
          // Unwrap the cause to throw the original exception for assertThrows
          throw e.getCause();
        }
      });
    }
  }
}