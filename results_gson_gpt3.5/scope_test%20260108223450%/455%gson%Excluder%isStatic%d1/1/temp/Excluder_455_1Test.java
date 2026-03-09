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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Excluder_455_1Test {

  private Excluder excluder;
  private Method isStaticMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    isStaticMethod = Excluder.class.getDeclaredMethod("isStatic", Class.class);
    isStaticMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsStaticWithStaticClass() throws Exception {
    // Static nested class for test
    class Outer {
      class Inner {}
    }
    // Create a static nested class dynamically via a static member class
    Class<?> staticNestedClass = StaticNested.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, staticNestedClass);
    assertTrue(result);
  }

  static class StaticNested {}

  @Test
    @Timeout(8000)
  void testIsStaticWithNonStaticClass() throws Exception {
    // Non-static inner class for test
    class Outer {
      class Inner {}
    }
    boolean result = (boolean) isStaticMethod.invoke(excluder, Outer.Inner.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsStaticWithTopLevelClass() throws Exception {
    // Top-level classes are never static
    boolean result = (boolean) isStaticMethod.invoke(excluder, String.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsStaticWithAnonymousClass() throws Exception {
    Object anon = new Object() {};
    boolean result = (boolean) isStaticMethod.invoke(excluder, anon.getClass());
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsStaticWithPrimitiveType() throws Exception {
    boolean result = (boolean) isStaticMethod.invoke(excluder, int.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsStaticWithArrayClass() throws Exception {
    boolean result = (boolean) isStaticMethod.invoke(excluder, String[].class);
    assertFalse(result);
  }
}