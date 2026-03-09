package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

public class JavaVersion_86_3Test {

    @Test
    @Timeout(8000)
    void testGetMajorJavaVersion_withVariousInputs() throws Exception {
        Method getMajorJavaVersionMethod = JavaVersion.class.getDeclaredMethod("getMajorJavaVersion", String.class);
        getMajorJavaVersionMethod.setAccessible(true);

        // Test dotted version "1.8.0_181"
        int version1 = (int) getMajorJavaVersionMethod.invoke(null, "1.8.0_181");
        assertEquals(8, version1);

        // Test dotted version "11.0.2"
        int version2 = (int) getMajorJavaVersionMethod.invoke(null, "11.0.2");
        assertEquals(11, version2);

        // Test simple version "9"
        int version3 = (int) getMajorJavaVersionMethod.invoke(null, "9");
        assertEquals(9, version3);

        // Test version with prefix "1.7"
        int version4 = (int) getMajorJavaVersionMethod.invoke(null, "1.7");
        assertEquals(7, version4);

        // Test null input
        int version5 = (int) getMajorJavaVersionMethod.invoke(null, (Object) null);
        assertEquals(6, version5);

        // Test empty string input
        int version6 = (int) getMajorJavaVersionMethod.invoke(null, "");
        assertEquals(6, version6);

        // Test unknown format
        int version7 = (int) getMajorJavaVersionMethod.invoke(null, "abc");
        assertEquals(6, version7);
    }

    @Test
    @Timeout(8000)
    void testParseDotted() throws Exception {
        Method parseDottedMethod = JavaVersion.class.getDeclaredMethod("parseDotted", String.class);
        parseDottedMethod.setAccessible(true);

        // "1.8.0_181" -> 8
        assertEquals(8, (int) parseDottedMethod.invoke(null, "1.8.0_181"));

        // "11.0.2" -> 11
        assertEquals(11, (int) parseDottedMethod.invoke(null, "11.0.2"));

        // "1.7" -> 7
        assertEquals(7, (int) parseDottedMethod.invoke(null, "1.7"));

        // "1" -> 1
        assertEquals(1, (int) parseDottedMethod.invoke(null, "1"));

        // null returns 6
        assertEquals(6, (int) parseDottedMethod.invoke(null, (Object) null));

        // empty string returns 6
        assertEquals(6, (int) parseDottedMethod.invoke(null, ""));
    }

    @Test
    @Timeout(8000)
    void testExtractBeginningInt() throws Exception {
        Method extractBeginningIntMethod = JavaVersion.class.getDeclaredMethod("extractBeginningInt", String.class);
        extractBeginningIntMethod.setAccessible(true);

        // "11.0.2" returns 11
        assertEquals(11, (int) extractBeginningIntMethod.invoke(null, "11.0.2"));

        // "9" returns 9
        assertEquals(9, (int) extractBeginningIntMethod.invoke(null, "9"));

        // "8_181" returns 8
        assertEquals(8, (int) extractBeginningIntMethod.invoke(null, "8_181"));

        // "abc" returns 6 (default)
        assertEquals(6, (int) extractBeginningIntMethod.invoke(null, "abc"));

        // empty string returns 6
        assertEquals(6, (int) extractBeginningIntMethod.invoke(null, ""));

        // null returns 6
        assertEquals(6, (int) extractBeginningIntMethod.invoke(null, (Object) null));
    }

    @Test
    @Timeout(8000)
    void testDetermineMajorJavaVersion_andGetMajorJavaVersion() {
        int majorVersion = JavaVersion.getMajorJavaVersion();
        assertTrue(majorVersion >= 6); // Should be at least 6 as per default in code
    }

    @Test
    @Timeout(8000)
    void testIsJava9OrLater() {
        int majorVersion = JavaVersion.getMajorJavaVersion();
        boolean expected = majorVersion >= 9;
        assertEquals(expected, JavaVersion.isJava9OrLater());
    }
}