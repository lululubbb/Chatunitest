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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Excluder_454_4Test {

  private Excluder excluder;

  @BeforeEach
  public void setUp() {
    excluder = new Excluder();
  }

  private boolean invokeIsInnerClass(Class<?> clazz) throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    method.setAccessible(true);
    return (boolean) method.invoke(excluder, clazz);
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_MemberStaticClass() throws Exception {
    // Static member class -> isInnerClass should be false
    Class<?> clazz = StaticMember.class;
    // Static member class should return false because isStatic(clazz) == true
    boolean result = invokeIsInnerClass(clazz);
    assertFalse(result);
  }

  static class StaticMember {}

  @Test
    @Timeout(8000)
  public void testIsInnerClass_MemberNonStaticClass() throws Exception {
    // Non-static member class -> isInnerClass should be true
    class Outer {
      class NonStaticMember {}
    }
    Class<?> clazz = Outer.class.getDeclaredClasses()[0]; // NonStaticMember.class;
    boolean result = invokeIsInnerClass(clazz);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_NonMemberClass() throws Exception {
    // Top-level class (this test class) is not a member class
    Class<?> clazz = this.getClass();
    boolean result = invokeIsInnerClass(clazz);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_AnonymousClass() throws Exception {
    // Anonymous class is not a member class
    Object anonymous = new Runnable() {
      @Override
      public void run() {}
    };
    Class<?> clazz = anonymous.getClass();
    boolean result = invokeIsInnerClass(clazz);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_PrivateStaticMethodIsStatic() throws Exception {
    // Use reflection to test isStatic private method for coverage completeness
    Method isStaticMethod = Excluder.class.getDeclaredMethod("isStatic", Class.class);
    isStaticMethod.setAccessible(true);

    class Outer {
      class NonStaticMember {}
    }
    Class<?> nonStaticMember = Outer.class.getDeclaredClasses()[0];

    // create a static member class as a top-level static class instead
    Class<?> staticMember = StaticMember.class;

    boolean staticResult = (boolean) isStaticMethod.invoke(excluder, staticMember);
    boolean nonStaticResult = (boolean) isStaticMethod.invoke(excluder, nonStaticMember);

    assertTrue(staticResult);
    assertFalse(nonStaticResult);
  }
}