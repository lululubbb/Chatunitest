package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BagOfPrimitivesDeserializationBenchmark_70_3Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;
  private Gson gsonMock;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
    gsonMock = mock(Gson.class);

    // inject gson mock
    java.lang.reflect.Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // inject json string
    java.lang.reflect.Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"a\":1,\"b\":true,\"c\":\"text\"}");
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_zeroReps() {
    benchmark.timeBagOfPrimitivesDefault(0);
    verify(gsonMock, times(0)).fromJson(Mockito.anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_oneRep() {
    benchmark.timeBagOfPrimitivesDefault(1);
    verify(gsonMock, times(1)).fromJson(Mockito.anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_multipleReps() {
    int reps = 5;
    benchmark.timeBagOfPrimitivesDefault(reps);
    verify(gsonMock, times(reps)).fromJson(Mockito.anyString(), eq(BagOfPrimitives.class));
  }
}