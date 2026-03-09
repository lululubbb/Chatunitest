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

public class Excluder_455_3Test {

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
    // Static nested class declared as a top-level class for test purposes
    class Outer {}
    Class<?> staticNestedClass = StaticNested.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, staticNestedClass);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withNonStaticClass() throws Exception {
    // Non-static inner class declared as a top-level class for test purposes
    Class<?> nonStaticInnerClass = NonStaticInner.class;
    boolean result = (boolean) isStaticMethod.invoke(excluder, nonStaticInnerClass);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withTopLevelClass() throws Exception {
    // Top-level class is never static
    boolean result = (boolean) isStaticMethod.invoke(excluder, String.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsStatic_withAnonymousClass() throws Exception {
    Object anon = new Object() {};
    boolean result = (boolean) isStaticMethod.invoke(excluder, anon.getClass());
    assertFalse(result);
  }

  // Static nested class moved outside test method to avoid compile error
  static class StaticNested {}

  // Non-static inner class moved outside test method to avoid compile error
  class NonStaticInner {}
}