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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Excluder_454_6Test {

  private Excluder excluder;
  private java.lang.reflect.Method isInnerClassMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    isInnerClassMethod = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    isInnerClassMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withMemberStaticClass() throws Exception {
    class Outer {
      // Remove static modifier, create a static inner class as a top-level class instead
    }
    // Define a static inner class as a top-level class inside the test method using a helper class
    Class<?> clazz = StaticInner.class;

    boolean result = (boolean) isInnerClassMethod.invoke(excluder, clazz);
    // Static inner class isMemberClass() == true but isStatic == true, so false expected
    assertFalse(result);
  }

  static class StaticInner {}

  @Test
    @Timeout(8000)
  void testIsInnerClass_withMemberNonStaticClass() throws Exception {
    class Outer {
      class Inner {}
    }
    Class<?> clazz = Outer.Inner.class;

    boolean result = (boolean) isInnerClassMethod.invoke(excluder, clazz);
    // Non-static inner class is member class and not static -> true expected
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withNonMemberClass() throws Exception {
    class LocalClass {}
    Class<?> clazz = LocalClass.class;

    boolean result = (boolean) isInnerClassMethod.invoke(excluder, clazz);
    // LocalClass is not a member class, so false expected
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withAnonymousClass() throws Exception {
    Object anonymous = new Object() {};
    Class<?> clazz = anonymous.getClass();

    boolean result = (boolean) isInnerClassMethod.invoke(excluder, clazz);
    // Anonymous classes are not member classes, so false expected
    assertFalse(result);
  }
}