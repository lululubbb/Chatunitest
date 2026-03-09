package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionsDeserializationBenchmark_411_6Test {

  private CollectionsDeserializationBenchmark benchmark;
  private Gson mockGson;
  private String testJson;
  private TypeToken<List<BagOfPrimitives>> listTypeToken;
  private Type listType;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    // Prepare test JSON string
    testJson = "[{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"test\"}]";

    // Inject gson mock and json string using reflection
    mockGson = Mockito.mock(Gson.class);
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, mockGson);

    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, testJson);

    // Access LIST_TYPE_TOKEN and LIST_TYPE via reflection
    Field listTypeTokenField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE_TOKEN");
    listTypeTokenField.setAccessible(true);
    listTypeToken = (TypeToken<List<BagOfPrimitives>>) listTypeTokenField.get(null);

    Field listTypeField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE");
    listTypeField.setAccessible(true);
    listType = (Type) listTypeField.get(null);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsDefault_invokesFromJsonCorrectNumberOfTimes() {
    int reps = 5;

    // Stub mockGson.fromJson to return null (we only verify calls)
    when(mockGson.fromJson(eq(testJson), eq(listTypeToken))).thenReturn(null);

    benchmark.timeCollectionsDefault(reps);

    // Verify fromJson called reps times with correct arguments
    verify(mockGson, times(reps)).fromJson(eq(testJson), eq(listTypeToken));
  }

  @Test
    @Timeout(8000)
  void timeCollectionsDefault_zeroReps_noInvocation() {
    int reps = 0;

    benchmark.timeCollectionsDefault(reps);

    // fromJson should never be called
    verify(mockGson, never()).fromJson(anyString(), any(TypeToken.class));
  }
}