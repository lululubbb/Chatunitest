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

public class Excluder_453_1Test {

  @Test
    @Timeout(8000)
  void isAnonymousOrNonStaticLocal_anonymousClass_returnsTrue() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> anonymousClass = new Object() {}.getClass();

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, anonymousClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAnonymousOrNonStaticLocal_localNonStaticClass_returnsTrue() throws Exception {
    Excluder excluder = new Excluder();

    class LocalNonStaticClass {}
    Class<?> localClass = LocalNonStaticClass.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, localClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAnonymousOrNonStaticLocal_staticLocalClass_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    // Static nested class declared at top-level to simulate static local class
    Class<?> staticNestedClass = StaticNested.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, staticNestedClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAnonymousOrNonStaticLocal_enumClass_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> enumClass = Thread.State.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, enumClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAnonymousOrNonStaticLocal_regularClass_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    Class<?> regularClass = String.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, regularClass);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAnonymousOrNonStaticLocal_nonStaticButNotLocalOrAnonymous_returnsFalse() throws Exception {
    Excluder excluder = new Excluder();

    // Non-static member class (inner class)
    class Outer {
      class Inner {}
    }
    Class<?> innerClass = Outer.Inner.class;

    Method method = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, innerClass);

    // The method checks isAnonymousClass() || isLocalClass(), inner class is neither
    // so should be false even if non-static
    assertFalse(result);
  }

  // Static nested class moved to top-level inside the test class to avoid illegal modifier error
  static class StaticNested {}
}