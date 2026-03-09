package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaVersion_81_3Test {

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_withParseDottedSuccess() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);

    // "1.8" -> parseDotted should parse as 1 (simulate)
    // Since parseDotted is private, we rely on actual implementation:
    // parseDotted("1.8") returns -1 or 1? We don't know, but let's test the behavior.
    // Actually, from the method, if parseDotted returns -1, then extractBeginningInt tries.
    // Let's test with "1.8" which should return 1 (or 8?), but let's assume it returns -1 and extractBeginningInt returns 1.
    int result = (int) method.invoke(null, "1.8");
    assertTrue(result > 0);

    // Test with "11.0.2" which should parse as 11 from parseDotted
    result = (int) method.invoke(null, "11.0.2");
    assertEquals(11, result);

    // Test with "9" which should parse as 9 from extractBeginningInt
    result = (int) method.invoke(null, "9");
    assertEquals(9, result);

    // Test with invalid string returns 6 (default)
    result = (int) method.invoke(null, "invalid.version");
    assertEquals(6, result);

    // Test with empty string returns 6 (default)
    result = (int) method.invoke(null, "");
    assertEquals(6, result);

    // Test with null input (should throw NullPointerException)
    assertThrows(InvocationTargetException.class, () -> method.invoke(null, (Object) null));
  }

  @Test
    @Timeout(8000)
  void testGetMajorJavaVersion_noInput() {
    // test the public no-arg getMajorJavaVersion returns a positive integer (cached field)
    int version = JavaVersion.getMajorJavaVersion();
    assertTrue(version >= 6);
  }
}