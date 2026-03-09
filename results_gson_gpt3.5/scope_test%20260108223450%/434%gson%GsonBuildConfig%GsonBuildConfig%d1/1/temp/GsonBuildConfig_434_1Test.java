package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class GsonBuildConfig_434_1Test {

    @Test
    @Timeout(8000)
    void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<GsonBuildConfig> constructor = GsonBuildConfig.class.getDeclaredConstructor();
        assertTrue(constructor.canAccess(null) == false);
        constructor.setAccessible(true);
        GsonBuildConfig instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    @Timeout(8000)
    void testVersionField() {
        assertEquals("2.10.1", GsonBuildConfig.VERSION);
    }
}