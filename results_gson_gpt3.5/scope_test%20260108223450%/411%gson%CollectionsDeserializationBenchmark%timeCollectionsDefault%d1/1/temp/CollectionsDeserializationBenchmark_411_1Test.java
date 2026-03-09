package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CollectionsDeserializationBenchmark_411_1Test {

  private CollectionsDeserializationBenchmark benchmark;
  private Gson gsonMock;
  private String jsonSample;
  private TypeToken<List<?>> listTypeToken;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();

    gsonMock = Mockito.mock(Gson.class);
    jsonSample = "[{\"intValue\":1,\"longValue\":2,\"stringValue\":\"test\"}]";

    // Inject gsonMock into private field 'gson'
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // Inject jsonSample into private field 'json'
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonSample);

    // Access private static final LIST_TYPE_TOKEN via reflection
    Field listTypeTokenField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE_TOKEN");
    listTypeTokenField.setAccessible(true);
    //noinspection unchecked
    listTypeToken = (TypeToken<List<?>>) listTypeTokenField.get(null);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_zeroReps() throws Exception {
    // Call with zero repetitions, should not invoke gson.fromJson
    benchmark.timeCollectionsDefault(0);

    // Verify gson.fromJson never called
    verify(gsonMock, times(0)).fromJson(Mockito.anyString(), Mockito.any(TypeToken.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_positiveReps() throws Exception {
    int reps = 3;

    // Call timeCollectionsDefault with positive reps
    benchmark.timeCollectionsDefault(reps);

    // Verify gson.fromJson called exactly 'reps' times with correct arguments
    verify(gsonMock, times(reps)).fromJson(eq(jsonSample), eq(listTypeToken));
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsDefault_oneRep() throws Exception {
    int reps = 1;

    benchmark.timeCollectionsDefault(reps);

    verify(gsonMock, times(1)).fromJson(eq(jsonSample), eq(listTypeToken));
  }
}