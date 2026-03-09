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

public class Excluder_453_5Test {

  private Excluder excluder;

  @BeforeEach
  public void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withAnonymousClass() throws Exception {
    Class<?> anonymousClass = new Object() {}.getClass();

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, anonymousClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withLocalClass() throws Exception {
    class LocalClass {}
    Class<?> localClass = LocalClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, localClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withStaticClass() throws Exception {
    Class<?> staticClass = StaticClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, staticClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withEnumClass() throws Exception {
    Class<?> enumClass = SampleEnum.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, enumClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withNonAnonymousNonLocalNonStaticClass() throws Exception {
    Class<?> normalClass = NormalClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, normalClass);

    assertFalse(result);
  }

  // Helper static class for testing
  static class StaticClass {}

  // Helper normal class for testing
  class NormalClass {}

  // Helper enum for testing
  enum SampleEnum {
    ONE, TWO;
  }
}