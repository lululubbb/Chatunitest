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

class Excluder_455_6Test {

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
    // Static nested class declared at top-level to allow static modifier
    class Outer {}
    class StaticNested extends Outer {}
    boolean result = (boolean) isStaticMethod.invoke(excluder, StaticNested.class);
    // StaticNested is not static, so expected false
    assertFalse(result);

    // Instead, use a top-level static nested class declared here:
    boolean result2 = (boolean) isStaticMethod.invoke(excluder, StaticNestedClass.class);
    assertTrue(result2);
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withNonStaticClass() throws Exception {
    class Outer {
      class Inner {}
    }
    boolean result = (boolean) isStaticMethod.invoke(excluder, Outer.Inner.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withTopLevelClass() throws Exception {
    boolean result = (boolean) isStaticMethod.invoke(excluder, String.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsStatic_withAnonymousClass() throws Exception {
    Object anon = new Object() {};
    boolean result = (boolean) isStaticMethod.invoke(excluder, anon.getClass());
    assertFalse(result);
  }

  // Static nested class declared as static top-level class
  static class StaticNestedClass {}
}