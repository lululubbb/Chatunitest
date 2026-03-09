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

public class Excluder_455_5Test {

  private Excluder excluder;
  private Method isStaticMethod;

  @BeforeEach
  public void setUp() throws Exception {
    excluder = new Excluder();
    isStaticMethod = Excluder.class.getDeclaredMethod("isStatic", Class.class);
    isStaticMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withStaticClass() throws Exception {
    // A static nested class for testing
    class OuterClass {
      class InnerClass {}
    }
    // Use a static nested class declared outside the method to avoid "static modifier not allowed" error
    Class<?> staticNestedClass = StaticNestedClass.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, staticNestedClass);
    assertTrue(result, "Static nested class should be detected as static");
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withNonStaticClass() throws Exception {
    // A non-static inner class for testing
    class OuterClass {
      class InnerClass {}
    }
    Class<?> innerClass = OuterClass.InnerClass.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, innerClass);
    assertFalse(result, "Non-static inner class should not be detected as static");
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withTopLevelClass() throws Exception {
    // Top level class (this test class) is not static
    boolean result = (boolean) isStaticMethod.invoke(excluder, this.getClass());
    assertFalse(result, "Top-level class should not be detected as static");
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withPrimitiveType() throws Exception {
    // Primitive types have no static modifier, expect false
    boolean result = (boolean) isStaticMethod.invoke(excluder, int.class);
    assertFalse(result, "Primitive type should not be detected as static");
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withAnonymousClass() throws Exception {
    Object anonymous = new Object() {};
    Class<?> anonymousClass = anonymous.getClass();
    boolean result = (boolean) isStaticMethod.invoke(excluder, anonymousClass);
    assertFalse(result, "Anonymous class should not be detected as static");
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withArrayClass() throws Exception {
    Class<?> arrayClass = String[].class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, arrayClass);
    // Array classes are not static classes themselves
    assertFalse(result, "Array class should not be detected as static");
  }

  // Static nested class declared at class level (not inside a method)
  static class StaticNestedClass {}
}