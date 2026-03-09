package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class $Gson$Preconditions_119_1Test {

    @Test
    @Timeout(8000)
    public void testCheckArgument_conditionTrue_noException() {
        // Should not throw any exception when condition is true
        assertDoesNotThrow(() -> $Gson$Preconditions.checkArgument(true));
    }

    @Test
    @Timeout(8000)
    public void testCheckArgument_conditionFalse_throwsIllegalArgumentException() {
        // Should throw IllegalArgumentException when condition is false
        assertThrows(IllegalArgumentException.class, () -> $Gson$Preconditions.checkArgument(false));
    }

    @Test
    @Timeout(8000)
    public void testPrivateConstructor() throws Exception {
        // Coverage for private constructor which throws UnsupportedOperationException
        Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InvocationTargetException e) {
            // The cause should be UnsupportedOperationException
            assertThrows(UnsupportedOperationException.class, () -> { throw e.getCause(); });
        }
    }

    @Test
    @Timeout(8000)
    public void testInvokeCheckArgumentViaReflection() throws Exception {
        Method method = $Gson$Preconditions.class.getDeclaredMethod("checkArgument", boolean.class);
        method.setAccessible(true);

        // condition = true, no exception
        method.invoke(null, true);

        // condition = false, expect InvocationTargetException wrapping IllegalArgumentException
        try {
            method.invoke(null, false);
        } catch (InvocationTargetException e) {
            assertThrows(IllegalArgumentException.class, () -> { throw e.getCause(); });
        }
    }
}