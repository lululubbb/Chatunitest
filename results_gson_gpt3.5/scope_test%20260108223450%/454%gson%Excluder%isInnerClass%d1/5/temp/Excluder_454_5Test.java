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

public class Excluder_454_5Test {

  @Test
    @Timeout(8000)
  void testIsInnerClass_withStaticMemberClass() throws Exception {
    class Outer {
      class StaticMember {}
    }
    Excluder excluder = Excluder.DEFAULT;
    Method method = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, Outer.StaticMember.class);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withNonStaticMemberClass() throws Exception {
    class Outer {
      class NonStaticMember {}
    }
    Excluder excluder = Excluder.DEFAULT;
    Method method = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, Outer.NonStaticMember.class);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsInnerClass_withNonMemberClass() throws Exception {
    Excluder excluder = Excluder.DEFAULT;
    Method method = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
    method.setAccessible(true);

    boolean result = (boolean) method.invoke(excluder, String.class);
    assertFalse(result);
  }
}