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

import org.junit.jupiter.api.Test;

public class Excluder_453_4Test {

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withAnonymousClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> anonymousClass = new Object() {}.getClass();

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, anonymousClass);

    assertTrue(result, "Anonymous class should be detected as anonymous or non-static local");
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withLocalClass() throws Exception {
    Excluder excluder = new Excluder();

    class LocalClass {}

    Class<?> localClass = LocalClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, localClass);

    // Local class is non-static by default, so isStatic should be false, and isLocalClass true
    assertTrue(result, "Local class should be detected as anonymous or non-static local");
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withStaticNestedClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> staticNestedClass = StaticNested.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, staticNestedClass);

    // Static nested class is static, so should return false
    assertFalse(result, "Static nested class should NOT be detected as anonymous or non-static local");
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withMemberClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> memberClass = MemberClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, memberClass);

    // Member class is non-static and not anonymous or local, so should return false
    assertFalse(result, "Non-static member class should NOT be detected as anonymous or non-static local");
  }

  @Test
    @Timeout(8000)
  public void testIsAnonymousOrNonStaticLocal_withEnumClass() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> enumClass = SampleEnum.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, enumClass);

    // Enum classes are excluded immediately, so should return false
    assertFalse(result, "Enum class should NOT be detected as anonymous or non-static local");
  }

  // Static nested class for test
  static class StaticNested {}

  // Non-static member class for test
  class MemberClass {}

  // Enum for test
  enum SampleEnum {
    A, B;
  }
}