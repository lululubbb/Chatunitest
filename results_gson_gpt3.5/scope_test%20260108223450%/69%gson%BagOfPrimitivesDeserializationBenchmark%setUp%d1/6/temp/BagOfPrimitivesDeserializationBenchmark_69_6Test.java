package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

class BagOfPrimitivesDeserializationBenchmark_69_6Test {

    private BagOfPrimitivesDeserializationBenchmark benchmark;

    @BeforeEach
    void init() {
        benchmark = new BagOfPrimitivesDeserializationBenchmark();
    }

    @Test
    @Timeout(8000)
    void testSetUp() throws Exception {
        // Invoke package-private method setUp() using reflection
        Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp");
        method.setAccessible(true);
        method.invoke(benchmark);

        // Validate gson field is initialized
        Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
        gsonField.setAccessible(true);
        Object gsonValue = gsonField.get(benchmark);
        assertNotNull(gsonValue);
        assertEquals(com.google.gson.Gson.class, gsonValue.getClass());

        // Validate json field is initialized correctly
        Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
        jsonField.setAccessible(true);
        Object jsonValue = jsonField.get(benchmark);
        assertNotNull(jsonValue);
        String jsonString = (String) jsonValue;
        assertTrue(jsonString.contains("\"longValue\":10"));
        assertTrue(jsonString.contains("\"intValue\":1"));
        assertTrue(jsonString.contains("\"booleanValue\":false"));
        assertTrue(jsonString.contains("\"stringValue\":\"foo\""));
    }
}