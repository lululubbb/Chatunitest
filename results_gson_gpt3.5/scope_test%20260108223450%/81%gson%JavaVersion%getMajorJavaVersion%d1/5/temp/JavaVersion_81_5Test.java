package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class JavaVersion_81_5Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withParseDottedSuccess() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // "1.8.0_201" should parse dotted as 8 (assuming parseDotted returns 8)
    // but since parseDotted is private, we test behavior by input that parseDotted returns not -1
    // We test with "11.0.2" which should parse as 11
    int result = (int) method.invoke(null, "11.0.2");
    assertEquals(11, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withExtractBeginningIntSuccess() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // Input that parseDotted returns -1 but extractBeginningInt returns a valid version
    // Since private methods, we try a string like "9" which should parse dotted fail and extract int 9
    int result = (int) method.invoke(null, "9");
    assertEquals(9, result);
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withBothFailing() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    // Input that both parseDotted and extractBeginningInt return -1, e.g. empty string or nonsense
    int result = (int) method.invoke(null, "nonsense");
    assertEquals(6, result);
  }
}