package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class NonUploadingCaliperRunner_312_5Test {

  @Test
    @Timeout(8000)
  void testConcat_noOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concat = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concat.setAccessible(true);

    String first = "first";
    String[] others = new String[0];
    String[] result = (String[]) concat.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(1, result.length);
    assertEquals("first", result[0]);
  }

  @Test
    @Timeout(8000)
  void testConcat_withOthers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concat = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concat.setAccessible(true);

    String first = "start";
    String[] others = new String[] {"one", "two", "three"};
    String[] result = (String[]) concat.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(4, result.length);
    assertEquals("start", result[0]);
    assertArrayEquals(new String[] {"one", "two", "three"}, java.util.Arrays.copyOfRange(result, 1, result.length));
  }

  @Test
    @Timeout(8000)
  void testConcat_withOneOther() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concat = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concat.setAccessible(true);

    String first = "alpha";
    String[] others = new String[] {"beta"};
    String[] result = (String[]) concat.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals("alpha", result[0]);
    assertEquals("beta", result[1]);
  }
}