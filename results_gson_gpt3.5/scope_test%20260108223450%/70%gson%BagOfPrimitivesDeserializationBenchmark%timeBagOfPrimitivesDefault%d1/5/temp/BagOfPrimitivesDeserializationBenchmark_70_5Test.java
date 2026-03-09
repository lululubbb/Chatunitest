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

public class BagOfPrimitivesDeserializationBenchmark_70_5Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;
  private Gson gsonMock;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    gsonMock = Mockito.mock(Gson.class);

    // Use reflection to set private gson field
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // Use reflection to set private json field to some non-null String
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"some\":\"json\"}");
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_zeroReps() {
    benchmark.timeBagOfPrimitivesDefault(0);
    // Verify no interactions with gson.fromJson if reps=0
    verify(gsonMock, times(0)).fromJson(anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_multipleReps() {
    int reps = 5;

    benchmark.timeBagOfPrimitivesDefault(reps);

    // Verify fromJson called reps times with correct arguments
    verify(gsonMock, times(reps)).fromJson("{\"some\":\"json\"}", BagOfPrimitives.class);
  }
}