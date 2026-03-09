package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class CollectionsDeserializationBenchmark_411_4Test {

  private CollectionsDeserializationBenchmark benchmark;
  private Gson gsonMock;
  private String jsonSample;
  private TypeToken<List<BagOfPrimitives>> listTypeToken;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Prepare Gson mock
    gsonMock = mock(Gson.class);

    // Set gson field via reflection
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // Prepare sample json string
    jsonSample = "[{\"anInt\":1,\"aLong\":2,\"aBoolean\":true,\"aString\":\"str\"}]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonSample);

    // Prepare LIST_TYPE_TOKEN field
    Field listTypeTokenField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE_TOKEN");
    listTypeTokenField.setAccessible(true);
    listTypeToken = (TypeToken<List<BagOfPrimitives>>) listTypeTokenField.get(null);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_zeroReps() {
    benchmark.timeCollectionsDefault(0);
    // Verify gson.fromJson never called
    verify(gsonMock, times(0)).fromJson(Mockito.anyString(), Mockito.any(TypeToken.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_positiveReps() {
    int reps = 5;
    benchmark.timeCollectionsDefault(reps);
    // Verify gson.fromJson called reps times with correct arguments
    verify(gsonMock, times(reps)).fromJson(eq(jsonSample), eq(listTypeToken));
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_oneRep() {
    benchmark.timeCollectionsDefault(1);
    verify(gsonMock, times(1)).fromJson(eq(jsonSample), eq(listTypeToken));
  }
}