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

class Excluder_454_1Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  private boolean invokeIsInnerClass(Class<?> clazz) throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    method.setAccessible(true);
    return (boolean) method.invoke(excluder, clazz);
  }

  static class StaticNestedClass {}

  class NonStaticInnerClass {}

  @Test
    @Timeout(8000)
  void testIsInnerClass_withStaticNestedClass() throws Exception {
    // Static nested class isMemberClass()=true but isStatic()=true => should return false
    boolean result = invokeIsInnerClass(StaticNestedClass.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withNonStaticInnerClass() throws Exception {
    // Non-static inner class isMemberClass()=true and isStatic()=false => should return true
    boolean result = invokeIsInnerClass(NonStaticInnerClass.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withTopLevelClass() throws Exception {
    // Top-level class isMemberClass()=false => should return false
    boolean result = invokeIsInnerClass(String.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withAnonymousClass() throws Exception {
    Object anonymous = new Object() {};
    boolean result = invokeIsInnerClass(anonymous.getClass());
    // anonymous class isMemberClass()=false => should return false
    assertFalse(result);
  }
}