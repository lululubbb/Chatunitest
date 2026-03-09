package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class GsonBuildConfig_434_4Test {

    @Test
    @Timeout(8000)
    public void testVERSIONConstant() {
        assertEquals("2.10.1", GsonBuildConfig.VERSION);
    }

    @Test
    @Timeout(8000)
    public void testPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<GsonBuildConfig> constructor = GsonBuildConfig.class.getDeclaredConstructor();
        assertNotNull(constructor);
        constructor.setAccessible(true);
        GsonBuildConfig instance = constructor.newInstance();
        assertNotNull(instance);
    }
}