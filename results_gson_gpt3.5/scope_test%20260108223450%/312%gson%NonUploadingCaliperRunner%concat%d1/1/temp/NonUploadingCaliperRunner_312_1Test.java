package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class NonUploadingCaliperRunner_312_1Test {

  @Test
    @Timeout(8000)
  void testConcatWithNoOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "first";
    String[] others = new String[0];

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals(first, result[0]);
  }

  @Test
    @Timeout(8000)
  void testConcatWithOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "first";
    String[] others = new String[] {"second", "third"};

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals(first, result[0]);
    assertEquals("second", result[1]);
    assertEquals("third", result[2]);
  }

  @Test
    @Timeout(8000)
  void testConcatWithOneOther() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "onlyFirst";
    String[] others = new String[] {"onlyOther"};

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals(first, result[0]);
    assertEquals("onlyOther", result[1]);
  }
}