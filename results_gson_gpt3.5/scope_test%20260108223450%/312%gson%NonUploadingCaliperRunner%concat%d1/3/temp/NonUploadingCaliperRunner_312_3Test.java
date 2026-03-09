package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class NonUploadingCaliperRunner_312_3Test {

  @Test
    @Timeout(8000)
  void testConcat_noOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "first";
    String[] others = new String[0];

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertArrayEquals(new String[] { first }, result);
  }

  @Test
    @Timeout(8000)
  void testConcat_withOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "first";
    String[] others = new String[] { "second", "third" };

    String[] expected = new String[] { "first", "second", "third" };
    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assertArrayEquals(expected, result);
  }
}