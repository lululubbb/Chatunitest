package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BagOfPrimitivesDeserializationBenchmark_70_4Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @Mock
  private Gson mockGson;

  private Class<?> bagOfPrimitivesClass;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Inject mock Gson into benchmark using reflection
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, mockGson);

    // Inject json string into benchmark using reflection
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"a\":1, \"b\":true, \"c\":\"string\"}");

    // Load BagOfPrimitives class as a top-level class from the same package
    bagOfPrimitivesClass = Class.forName("com.google.gson.metrics.BagOfPrimitives");
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_invokesFromJsonCorrectNumberOfTimes() throws Exception {
    int reps = 5;

    // Stub mockGson.fromJson to return a new BagOfPrimitives instance
    when(mockGson.<Object>fromJson(anyString(), eq(bagOfPrimitivesClass)))
        .thenReturn(bagOfPrimitivesClass.getDeclaredConstructor().newInstance());

    // Use reflection to invoke the public method
    Method timeMethod = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesDefault", int.class);
    timeMethod.setAccessible(true);
    timeMethod.invoke(benchmark, reps);

    // Verify fromJson called reps times with correct arguments
    verify(mockGson, times(reps)).fromJson("{\"a\":1, \"b\":true, \"c\":\"string\"}", bagOfPrimitivesClass);
  }
}