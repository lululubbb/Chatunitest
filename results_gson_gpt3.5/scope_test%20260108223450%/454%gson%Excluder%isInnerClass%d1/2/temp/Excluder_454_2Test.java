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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Excluder_454_2Test {
  private Excluder excluder;
  private Method isInnerClassMethod;

  static class StaticMemberClass {}

  class NonStaticMemberClass {}

  @BeforeEach
  public void setUp() throws Exception {
    excluder = new Excluder();
    isInnerClassMethod = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    isInnerClassMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withStaticMemberClass() throws Exception {
    boolean result = (boolean) isInnerClassMethod.invoke(excluder, StaticMemberClass.class);
    assertFalse(result, "Static member class should not be considered inner class");
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withNonStaticMemberClass() throws Exception {
    boolean result = (boolean) isInnerClassMethod.invoke(excluder, NonStaticMemberClass.class);
    assertTrue(result, "Non-static member class should be considered inner class");
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withTopLevelClass() throws Exception {
    boolean result = (boolean) isInnerClassMethod.invoke(excluder, Excluder.class);
    assertFalse(result, "Top-level class should not be considered inner class");
  }

  @Test
    @Timeout(8000)
  public void testIsInnerClass_withAnonymousClass() throws Exception {
    Object anonymousInstance = new Object() {};
    Class<?> anonymousClass = anonymousInstance.getClass();
    boolean result = (boolean) isInnerClassMethod.invoke(excluder, anonymousClass);
    // anonymous classes are not member classes, so isInnerClass returns false
    // but Excluder treats anonymous classes as inner classes via isAnonymousOrNonStaticLocal()
    // so we accept false here as per isInnerClass implementation
    assertFalse(result, "Anonymous class should not be considered inner class by isInnerClass");
  }
}