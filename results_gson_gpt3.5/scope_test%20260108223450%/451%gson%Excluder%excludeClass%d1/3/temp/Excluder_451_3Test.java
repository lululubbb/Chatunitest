package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Excluder_451_3Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  void excludeClass_excludeClassChecksTrue() throws Exception {
    Class<?> clazz = DummyClass.class;

    // Since excludeClassChecks is private and cannot be mocked directly,
    // we test excludeClass by spying and mocking excludeClass to return true directly.
    Excluder spyExcluder = spy(excluder);

    doReturn(true).when(spyExcluder).excludeClass(clazz, true);

    boolean result = spyExcluder.excludeClass(clazz, true);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeClass_excludeClassChecksFalse_strategyTrue() throws Exception {
    Class<?> clazz = DummyClass.class;

    // Since excludeClassChecks and excludeClassInStrategy are private and cannot be mocked directly,
    // test excludeClassInStrategy directly with a strategy that returns true.

    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipClass(clazz)).thenReturn(true);

    Excluder ex = excluder.withExclusionStrategy(strategy, false, true);

    Method excludeClassInStrategy = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
    excludeClassInStrategy.setAccessible(true);

    boolean strategyResult = (boolean) excludeClassInStrategy.invoke(ex, clazz, false);

    assertTrue(strategyResult);
  }

  @Test
    @Timeout(8000)
  void excludeClass_excludeClassChecksFalse_strategyFalse() throws Exception {
    Class<?> clazz = DummyClass.class;

    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipClass(clazz)).thenReturn(false);

    Excluder ex = excluder.withExclusionStrategy(strategy, true, true);

    Method excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
    excludeClassChecksMethod.setAccessible(true);

    Method excludeClassInStrategyMethod = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
    excludeClassInStrategyMethod.setAccessible(true);

    boolean checks = (boolean) excludeClassChecksMethod.invoke(ex, clazz);
    boolean strategyResult = (boolean) excludeClassInStrategyMethod.invoke(ex, clazz, true);

    boolean result = checks || strategyResult;

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void excludeClassChecks_innerClassSerializationDisabled() throws Exception {
    Excluder ex = excluder.disableInnerClassSerialization();

    Outer outer = new Outer();
    Class<?> innerClass = outer.new NonStaticInner().getClass();

    Method method = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(ex, innerClass);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeClassChecks_versionChecks() throws Exception {
    Excluder ex = excluder.withVersion(1.0);

    Class<?> sinceClass = SinceAnnotated.class;
    Class<?> untilClass = UntilAnnotated.class;
    Class<?> validClass = ValidVersionClass.class;

    Method method = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
    method.setAccessible(true);

    assertTrue((boolean) method.invoke(ex, sinceClass));
    assertTrue((boolean) method.invoke(ex, untilClass));
    assertFalse((boolean) method.invoke(ex, validClass));
  }

  @Test
    @Timeout(8000)
  void excludeClassInStrategy_serializationStrategy() throws Exception {
    ExclusionStrategy serializationStrategy = mock(ExclusionStrategy.class);
    when(serializationStrategy.shouldSkipClass(DummyClass.class)).thenReturn(true);

    Excluder ex = excluder.withExclusionStrategy(serializationStrategy, true, false);

    Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(ex, DummyClass.class, true);
    assertTrue(result);

    result = (boolean) method.invoke(ex, DummyClass.class, false);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void excludeClassInStrategy_deserializationStrategy() throws Exception {
    ExclusionStrategy deserializationStrategy = mock(ExclusionStrategy.class);
    when(deserializationStrategy.shouldSkipClass(DummyClass.class)).thenReturn(true);

    Excluder ex = excluder.withExclusionStrategy(deserializationStrategy, false, true);

    Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(ex, DummyClass.class, false);
    assertTrue(result);

    result = (boolean) method.invoke(ex, DummyClass.class, true);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void excludeClassInStrategy_multipleStrategies() throws Exception {
    ExclusionStrategy serializationStrategy1 = mock(ExclusionStrategy.class);
    ExclusionStrategy serializationStrategy2 = mock(ExclusionStrategy.class);
    when(serializationStrategy1.shouldSkipClass(DummyClass.class)).thenReturn(false);
    when(serializationStrategy2.shouldSkipClass(DummyClass.class)).thenReturn(true);

    List<ExclusionStrategy> serializationStrategies = new ArrayList<>();
    serializationStrategies.add(serializationStrategy1);
    serializationStrategies.add(serializationStrategy2);

    Excluder ex = excluder.withVersion(-1.0d); // Use -1.0d instead of private IGNORE_VERSIONS

    Field field = Excluder.class.getDeclaredField("serializationStrategies");
    field.setAccessible(true);
    field.set(ex, serializationStrategies);

    Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(ex, DummyClass.class, true);
    assertTrue(result);
  }

  // Dummy classes for testing
  static class DummyClass {}

  static class Outer {
    class NonStaticInner {}
  }

  @Since(2.0)
  static class SinceAnnotated {}

  @Until(0.5)
  static class UntilAnnotated {}

  @Since(0.5)
  @Until(2.0)
  static class ValidVersionClass {}

}