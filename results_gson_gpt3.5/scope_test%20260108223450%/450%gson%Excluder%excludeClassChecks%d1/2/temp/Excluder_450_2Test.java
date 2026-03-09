package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.annotations.Since;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Excluder_450_2Test {

    private Excluder excluder;
    private Method excludeClassChecksMethod;

    @BeforeEach
    void setUp() throws Exception {
        excluder = new Excluder();
        excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecksMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_VersionIgnored_ReturnsFalse() throws Exception {
        // Use this.getClass() to get the current test class instead of Class.forName
        Class<?> clazz = this.getClass();
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_VersionInvalid_ReturnsTrue() throws Exception {
        excluder = excluder.withVersion(1.0);

        @Since(2.0)
        class Since2 {}

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, Since2.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_VersionValid_InnerClassSerializationDisabled_ReturnsTrue() throws Exception {
        excluder = excluder.withVersion(2.0).disableInnerClassSerialization();

        class InnerClass {}

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, InnerClass.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_VersionValid_InnerClassSerializationEnabled_AnonymousOrNonStaticLocal_ReturnsTrue() throws Exception {
        excluder = excluder.withVersion(2.0);

        Object anonymous = new Runnable() {
            @Override public void run() {}
        };

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, anonymous.getClass());
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_VersionValid_InnerClassSerializationEnabled_StaticClass_ReturnsFalse() throws Exception {
        excluder = excluder.withVersion(2.0);

        Class<?> staticNestedClass = StaticNested.class;

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, staticNestedClass);
        assertFalse(result);
    }

    static class StaticNested {
    }

}