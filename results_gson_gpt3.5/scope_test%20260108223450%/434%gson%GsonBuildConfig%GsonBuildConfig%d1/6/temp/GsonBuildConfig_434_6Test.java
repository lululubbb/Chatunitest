package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class GsonBuildConfig_434_6Test {

    @Test
    @Timeout(8000)
    public void testVERSIONValue() {
        assertEquals("2.10.1", GsonBuildConfig.VERSION);
    }

    @Test
    @Timeout(8000)
    public void testPrivateConstructor() throws Exception {
        var constructor = GsonBuildConfig.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        var instance = constructor.newInstance();
        // instance is not null to ensure constructor invocation
        assertEquals(GsonBuildConfig.class, instance.getClass());
    }
}