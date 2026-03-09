package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JavaVersion_85_1Test {

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIs8() throws Exception {
    setMajorJavaVersion(8);
    assertFalse(JavaVersion.isJava9OrLater());
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIs9() throws Exception {
    setMajorJavaVersion(9);
    assertTrue(JavaVersion.isJava9OrLater());
  }

  @Test
    @Timeout(8000)
  public void testIsJava9OrLater_whenVersionIs11() throws Exception {
    setMajorJavaVersion(11);
    assertTrue(JavaVersion.isJava9OrLater());
  }

  @Test
    @Timeout(8000)
  public void testDetermineMajorJavaVersion_privateMethod() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion");
    method.setAccessible(true);
    int version = (int) method.invoke(null);
    assertTrue(version > 0);
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_withVersionString() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
    method.setAccessible(true);
    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_191")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(7, ((Integer) method.invoke(null, "7")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testParseDotted_privateMethod() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
    method.setAccessible(true);
    assertEquals(8, ((Integer) method.invoke(null, "1.8.0_191")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testExtractBeginningInt_privateMethod() throws Exception {
    Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
    method.setAccessible(true);
    assertEquals(1, ((Integer) method.invoke(null, "1.8.0_191")).intValue());
    assertEquals(9, ((Integer) method.invoke(null, "9")).intValue());
    assertEquals(11, ((Integer) method.invoke(null, "11.0.2")).intValue());
    assertEquals(-1, ((Integer) method.invoke(null, "abc")).intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetMajorJavaVersion_publicMethod() throws Exception {
    // Should return the current static majorJavaVersion field value
    setMajorJavaVersion(15);
    assertEquals(15, JavaVersion.getMajorJavaVersion());
  }

  private void setMajorJavaVersion(int version) throws Exception {
    Field field = JavaVersion.class.getDeclaredField("majorJavaVersion");
    field.setAccessible(true);

    // Remove final modifier via reflection hack
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(null, version);
  }
}