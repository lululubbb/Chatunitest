package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class NonUploadingCaliperRunner_312_2Test {

  @Test
    @Timeout(8000)
  void testConcatWithNoOthers() throws Exception {
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
  void testConcatWithOthers() throws Exception {
    Method concat = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concat.setAccessible(true);

    String first = "first";
    String[] others = new String[] {"second", "third"};
    String[] result = (String[]) concat.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("first", result[0]);
    assertEquals("second", result[1]);
    assertEquals("third", result[2]);
  }

  @Test
    @Timeout(8000)
  void testConcatWithOneOther() throws Exception {
    Method concat = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concat.setAccessible(true);

    String first = "start";
    String[] others = new String[] {"end"};
    String[] result = (String[]) concat.invoke(null, first, others);

    assertNotNull(result);
    assertEquals(2, result.length);
    assertEquals("start", result[0]);
    assertEquals("end", result[1]);
  }
}