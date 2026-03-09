package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class $Gson$Preconditions_117_3Test {

    @Test
    @Timeout(8000)
    public void testConstructor_throwsUnsupportedOperationException() throws Exception {
        Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
        assertNotNull(thrown);
        assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
    }

    @Test
    @Timeout(8000)
    public void testCheckNotNull_withNonNullObject_returnsSameObject() {
        String testString = "test";
        String result = $Gson$Preconditions.checkNotNull(testString);
        assertSame(testString, result);
    }

    @Test
    @Timeout(8000)
    public void testCheckNotNull_withNullObject_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            $Gson$Preconditions.checkNotNull(null);
        });
    }

    @Test
    @Timeout(8000)
    public void testCheckArgument_withTrueCondition_doesNotThrow() {
        assertDoesNotThrow(() -> {
            $Gson$Preconditions.checkArgument(true);
        });
    }

    @Test
    @Timeout(8000)
    public void testCheckArgument_withFalseCondition_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            $Gson$Preconditions.checkArgument(false);
        });
    }
}