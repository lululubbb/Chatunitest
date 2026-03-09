package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class CollectionsDeserializationBenchmark_411_3Test {

  private CollectionsDeserializationBenchmark benchmark;
  private Gson mockGson;
  private String sampleJson;
  private TypeToken<List<BagOfPrimitives>> listTypeToken;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Prepare mock Gson
    mockGson = Mockito.mock(Gson.class);

    // Sample JSON string
    sampleJson = "[{\"intValue\":1,\"longValue\":2,\"stringValue\":\"test\"}]";

    // Inject mock gson and json string into benchmark instance using reflection
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, mockGson);

    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, sampleJson);

    // Access LIST_TYPE_TOKEN field for verification
    Field listTypeTokenField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE_TOKEN");
    listTypeTokenField.setAccessible(true);
    listTypeToken = (TypeToken<List<BagOfPrimitives>>) listTypeTokenField.get(null);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_invokesFromJsonCorrectNumberOfTimes() {
    int reps = 5;

    // Stub mockGson.fromJson to return null or a dummy list
    when(mockGson.fromJson(eq(sampleJson), eq(listTypeToken))).thenReturn(null);

    // Call the method under test
    benchmark.timeCollectionsDefault(reps);

    // Verify fromJson called exactly reps times with correct arguments
    verify(mockGson, times(reps)).fromJson(sampleJson, listTypeToken);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_zeroReps_noInvocation() {
    int reps = 0;

    benchmark.timeCollectionsDefault(reps);

    // Verify fromJson is never called when reps=0
    verify(mockGson, never()).fromJson(anyString(), any(TypeToken.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_negativeReps_noInvocation() {
    int reps = -1;

    benchmark.timeCollectionsDefault(reps);

    // Verify fromJson is never called when reps negative
    verify(mockGson, never()).fromJson(anyString(), any(TypeToken.class));
  }
}