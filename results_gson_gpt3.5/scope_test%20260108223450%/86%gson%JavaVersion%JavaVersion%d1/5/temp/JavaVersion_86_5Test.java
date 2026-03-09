package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JavaVersion_86_5Test {

    private int invokeIntMethod(Method method, Object... args) throws Exception {
        Object result = method.invoke(null, args);
        if (result instanceof Integer) {
            return (Integer) result;
        } else if (result instanceof Number) {
            return ((Number) result).intValue();
        } else if (result == null) {
            return 0;
        } else {
            fail("Expected method to return an integer but got: " + result.getClass());
            return -1; // unreachable
        }
    }

    @Test
    @Timeout(8000)
    public void testDetermineMajorJavaVersion() throws Exception {
        Method method = JavaVersion.class.getDeclaredMethod("determineMajorJavaVersion", String.class);
        method.setAccessible(true);
        int version = invokeIntMethod(method, System.getProperty("java.version"));
        assertTrue(version > 0, "Major Java version should be positive");
    }

    @Test
    @Timeout(8000)
    public void testGetMajorJavaVersion_String() throws Exception {
        Method method = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
        method.setAccessible(true);

        assertEquals(8, invokeIntMethod(method, "1.8.0_151"));
        assertEquals(9, invokeIntMethod(method, "9"));
        assertEquals(11, invokeIntMethod(method, "11.0.2"));
        assertEquals(17, invokeIntMethod(method, "17"));
        assertEquals(0, invokeIntMethod(method, (Object) "abc"));
        assertEquals(0, invokeIntMethod(method, (Object) ""));
        assertEquals(0, invokeIntMethod(method, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testParseDotted() throws Exception {
        Method method = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
        method.setAccessible(true);

        assertEquals(8, invokeIntMethod(method, "1.8.0_151"));
        assertEquals(9, invokeIntMethod(method, "9"));
        assertEquals(11, invokeIntMethod(method, "11.0.2"));
        assertEquals(0, invokeIntMethod(method, (Object) "abc"));
        assertEquals(0, invokeIntMethod(method, (Object) ""));
        assertEquals(0, invokeIntMethod(method, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testExtractBeginningInt() throws Exception {
        Method method = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
        method.setAccessible(true);

        assertEquals(1, invokeIntMethod(method, "1.8.0_151"));
        assertEquals(9, invokeIntMethod(method, "9"));
        assertEquals(11, invokeIntMethod(method, "11.0.2"));
        assertEquals(0, invokeIntMethod(method, (Object) "abc"));
        assertEquals(0, invokeIntMethod(method, (Object) ""));
        assertEquals(0, invokeIntMethod(method, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testGetMajorJavaVersion_NoArgs() {
        int version = JavaVersion.getMajorJavaVersion();
        assertTrue(version > 0, "Major Java version should be positive");
    }

    @Test
    @Timeout(8000)
    public void testIsJava9OrLater() {
        boolean result = JavaVersion.isJava9OrLater();
        int version = JavaVersion.getMajorJavaVersion();
        if (version >= 9) {
            assertTrue(result);
        } else {
            assertFalse(result);
        }
    }
}