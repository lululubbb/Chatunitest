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

class Excluder_455_4Test {

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
  void testIsStatic_withStaticClass() throws Exception {
    // Static nested class declared at top level
    class Outer {}
    // Declare static nested class as a top-level static class in this test file
    // Instead, create a static nested class dynamically via a helper class
    Class<?> staticNestedClass = StaticNested.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, staticNestedClass);
    assertTrue(result, "Static nested class should be detected as static");
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withNonStaticClass() throws Exception {
    // Non-static inner class declared inside a top-level class
    Class<?> innerClass = Outer.Inner.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, innerClass);
    assertFalse(result, "Non-static inner class should not be detected as static");
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withTopLevelClass() throws Exception {
    // Top-level class is never static
    boolean result = (boolean) isStaticMethod.invoke(excluder, String.class);
    assertFalse(result, "Top-level class should not be detected as static");
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withAnonymousClass() throws Exception {
    Object anon = new Object() {};
    boolean result = (boolean) isStaticMethod.invoke(excluder, anon.getClass());
    assertFalse(result, "Anonymous class should not be detected as static");
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withPrimitiveType() throws Exception {
    boolean result = (boolean) isStaticMethod.invoke(excluder, int.class);
    assertFalse(result, "Primitive type should not be detected as static");
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withArrayClass() throws Exception {
    boolean result = (boolean) isStaticMethod.invoke(excluder, String[].class);
    assertFalse(result, "Array class should not be detected as static");
  }

  // Helper static nested class declared at top level
  static class StaticNested {}

  // Helper outer class with non-static inner class declared at top level
  static class Outer {
    class Inner {}
  }
}