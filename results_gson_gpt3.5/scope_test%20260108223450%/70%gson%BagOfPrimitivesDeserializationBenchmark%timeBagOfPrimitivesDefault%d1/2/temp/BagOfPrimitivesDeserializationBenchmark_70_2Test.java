package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class BagOfPrimitivesDeserializationBenchmark_70_2Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;
  private Gson mockGson;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Create mock Gson
    mockGson = Mockito.mock(Gson.class);

    // Inject mock Gson into benchmark instance via reflection
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, mockGson);

    // Inject sample JSON string into benchmark instance via reflection
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"a\":1,\"b\":true,\"c\":\"text\"}");
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_zeroReps() {
    // reps = 0, so fromJson should never be called
    benchmark.timeBagOfPrimitivesDefault(0);
    verify(mockGson, times(0)).fromJson(anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_positiveReps() {
    int reps = 5;
    benchmark.timeBagOfPrimitivesDefault(reps);
    verify(mockGson, times(reps)).fromJson(anyString(), eq(BagOfPrimitives.class));
  }

}