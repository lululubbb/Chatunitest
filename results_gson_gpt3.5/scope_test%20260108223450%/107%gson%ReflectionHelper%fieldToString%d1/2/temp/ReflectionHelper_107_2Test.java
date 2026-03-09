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

public class ReflectionHelper_107_2Test {

  private static class DummyClass {
    private int dummyField;
  }

  @Test
    @Timeout(8000)
  public void testFieldToString() throws NoSuchFieldException {
    Field field = DummyClass.class.getDeclaredField("dummyField");
    String expected = DummyClass.class.getName() + "#" + "dummyField";
    String actual = ReflectionHelper.fieldToString(field);
    assertEquals(expected, actual);
  }
}