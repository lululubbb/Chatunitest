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

public class Excluder_453_2Test {

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withAnonymousClass_returnsTrue() throws Exception {
    Excluder excluder = new Excluder();

    // Create an anonymous class instance
    Object anonymousInstance = new Object() {
    };
    Class<?> anonymousClass = anonymousInstance.getClass();

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, anonymousClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withLocalClassNonStatic_returnsTrue() throws Exception {
    Excluder excluder = new Excluder();

    class LocalClass {
    }
    Class<?> localClass = LocalClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, localClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withStaticLocalClass_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    // Static local class simulation: static nested class declared at top level of test class
    Class<?> staticNestedClass = StaticNested.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, staticNestedClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withEnum_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> enumClass = Thread.State.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, enumClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsAnonymousOrNonStaticLocal_withRegularClass_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> stringClass = String.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, stringClass);

    assertFalse(result);
  }

  // Moved static nested class to top level of test class (not inside method)
  static class StaticNested {
  }
}