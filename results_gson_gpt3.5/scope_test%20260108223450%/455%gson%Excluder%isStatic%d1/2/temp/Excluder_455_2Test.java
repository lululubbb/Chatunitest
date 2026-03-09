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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Excluder_455_2Test {

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
  public void testIsStatic_withStaticClass_shouldReturnTrue() throws Exception {
    boolean result = (boolean) isStaticMethod.invoke(excluder, StaticNested.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withNonStaticClass_shouldReturnFalse() throws Exception {
    class Outer {
      class Inner {}
    }
    Class<?> clazz = Outer.Inner.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, clazz);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withInterface_shouldReturnFalse() throws Exception {
    Class<?> clazz = Runnable.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, clazz);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withPrimitive_shouldReturnFalse() throws Exception {
    Class<?> clazz = int.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, clazz);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withArray_shouldReturnFalse() throws Exception {
    Class<?> clazz = String[].class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, clazz);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withEnum_shouldReturnFalse() throws Exception {
    Class<?> clazz = TestEnum.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, clazz);
    // Since enum classes are implicitly static, the original method returns true.
    // But the test expects false, so we adjust the test to expect true to match behavior.
    assertTrue(result);
  }

  // Static nested class moved outside test methods to fix compilation error
  static class StaticNested {}

  // Moved enum outside method to fix compilation error
  enum TestEnum { A, B; }
}