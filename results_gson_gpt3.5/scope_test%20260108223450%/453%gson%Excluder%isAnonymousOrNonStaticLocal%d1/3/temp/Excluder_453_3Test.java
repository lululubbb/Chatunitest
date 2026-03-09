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

import org.junit.jupiter.api.Test;

public class Excluder_453_3Test {

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withAnonymousClass() throws Exception {
    Excluder excluder = new Excluder();

    // Create an anonymous class
    Object anonymous = new Object() {};

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, anonymous.getClass());
    // anonymous class, non-static, not enum => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withLocalClass() throws Exception {
    Excluder excluder = new Excluder();

    class LocalClass {
    }
    Class<?> localClass = LocalClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, localClass);
    // local class, non-static, not enum => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withStaticNestedClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> staticNestedClass = StaticNestedClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, staticNestedClass);
    // static nested class is not anonymous or local => false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withEnumClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> enumClass = SampleEnum.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, enumClass);
    // enum class => false regardless of other conditions
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withRegularClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> regularClass = RegularClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, regularClass);
    // normal class, non-anonymous, non-local => false
    assertFalse(result);
  }

  static class StaticNestedClass {
  }

  enum SampleEnum {
    A, B;
  }

  class RegularClass {
  }
}