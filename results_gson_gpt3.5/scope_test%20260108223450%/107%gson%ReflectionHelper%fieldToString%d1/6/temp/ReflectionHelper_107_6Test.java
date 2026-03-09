package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class ReflectionHelper_107_6Test {

  @Test
    @Timeout(8000)
  void testFieldToString() throws NoSuchFieldException {
    // Use a sample class to get a Field instance
    class Sample {
      private int sampleField;
    }

    Field field = Sample.class.getDeclaredField("sampleField");
    String expected = Sample.class.getName() + "#sampleField";

    String actual = ReflectionHelper.fieldToString(field);

    assertEquals(expected, actual);
  }
}