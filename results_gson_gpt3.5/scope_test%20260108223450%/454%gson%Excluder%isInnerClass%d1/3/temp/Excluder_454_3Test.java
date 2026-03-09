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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Excluder_454_3Test {

  private Excluder excluder;
  private Method isInnerClassMethod;

  @BeforeEach
  public void setUp() throws Exception {
    excluder = new Excluder();
    isInnerClassMethod = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    isInnerClassMethod.setAccessible(true);
  }

  private boolean invokeIsInnerClass(Class<?> clazz) throws Exception {
    return (boolean) isInnerClassMethod.invoke(excluder, clazz);
  }

  // Helper static inner class (non-static)
  class NonStaticInnerClass {}

  // Helper static inner class (static)
  static class StaticInnerClass {}

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withNonStaticMemberClass() throws Exception {
    // Non-static inner class isMemberClass() == true and isStatic() == false
    Class<?> clazz = NonStaticInnerClass.class;
    assertTrue(clazz.isMemberClass());
    assertFalse(isStatic(clazz));
    assertTrue(invokeIsInnerClass(clazz));
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withStaticMemberClass() throws Exception {
    // Static inner class isMemberClass() == true and isStatic() == true
    Class<?> clazz = StaticInnerClass.class;
    assertTrue(clazz.isMemberClass());
    assertTrue(isStatic(clazz));
    assertFalse(invokeIsInnerClass(clazz));
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withTopLevelClass() throws Exception {
    // Top-level class isMemberClass() == false
    Class<?> clazz = String.class; // String is top-level class
    assertFalse(clazz.isMemberClass());
    assertFalse(invokeIsInnerClass(clazz));
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withAnonymousClass() throws Exception {
    Object anon = new Runnable() {
      @Override public void run() {}
    };
    Class<?> clazz = anon.getClass();
    // Anonymous classes are not member classes
    assertFalse(clazz.isMemberClass());
    assertFalse(invokeIsInnerClass(clazz));
  }

  // Reflection helper to invoke private isStatic method for assertions
  private boolean isStatic(Class<?> clazz) throws Exception {
    Method isStaticMethod = Excluder.class.getDeclaredMethod("isStatic", Class.class);
    isStaticMethod.setAccessible(true);
    return (boolean) isStaticMethod.invoke(excluder, clazz);
  }
}