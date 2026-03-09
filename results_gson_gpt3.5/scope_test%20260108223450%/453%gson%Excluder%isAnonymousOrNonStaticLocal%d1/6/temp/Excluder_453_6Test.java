package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

public class Excluder_453_6Test {

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withAnonymousClass() throws Exception {
    Excluder excluder = new Excluder();

    // Create an anonymous class instance
    Object anonymousInstance = new Runnable() {
      @Override
      public void run() {}
    };
    Class<?> anonymousClass = anonymousInstance.getClass();

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, anonymousClass);

    // anonymous class, non-static, not enum -> true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withLocalClass() throws Exception {
    Excluder excluder = new Excluder();

    class LocalClass {}

    Class<?> localClass = LocalClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, localClass);

    // local class, non-static, not enum -> true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withStaticNestedClass() throws Exception {
    Excluder excluder = new Excluder();

    class Outer {
      class StaticNested {}
    }

    // Use reflection to get the nested class and check if it is static
    Class<?>[] declaredClasses = Outer.class.getDeclaredClasses();
    Class<?> staticNestedClass = null;
    for (Class<?> c : declaredClasses) {
      if ("StaticNested".equals(c.getSimpleName())) {
        staticNestedClass = c;
        break;
      }
    }
    assertNotNull(staticNestedClass);

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, staticNestedClass);

    // static nested class -> isStatic returns true -> false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withNonStaticInnerClass() throws Exception {
    Excluder excluder = new Excluder();

    class Outer {
      class Inner {}
    }

    Class<?> innerClass = Outer.Inner.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, innerClass);

    // non-static inner class, not anonymous nor local, so false
    // but isStatic(innerClass) returns false and (anonymous or local) is false -> overall false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withEnumClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> enumClass = Thread.State.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, enumClass);

    // Enum class -> first check fails, returns false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withRegularClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> regularClass = String.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, regularClass);

    // String class is not anonymous, not local, isStatic probably true (static top-level), not enum
    // So returns false
    assertFalse(result);
  }
}