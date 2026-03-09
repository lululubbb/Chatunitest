package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class $Gson$Preconditions_119_6Test {

    @Test
    @Timeout(8000)
    void checkArgument_withTrueCondition_doesNotThrow() {
        // Should not throw any exception
        $Gson$Preconditions.checkArgument(true);
    }

    @Test
    @Timeout(8000)
    void checkArgument_withFalseCondition_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> $Gson$Preconditions.checkArgument(false));
    }

    @Test
    @Timeout(8000)
    void privateConstructor_invocation() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause() instanceof UnsupportedOperationException);
        }
    }
}