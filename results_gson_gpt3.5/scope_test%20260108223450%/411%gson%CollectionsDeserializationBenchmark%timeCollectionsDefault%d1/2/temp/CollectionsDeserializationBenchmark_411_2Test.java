package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class CollectionsDeserializationBenchmark_411_2Test {

  @Mock
  private Gson gsonMock;

  private CollectionsDeserializationBenchmark benchmark;

  private TypeToken<?> listTypeToken;

  @BeforeEach
  void setUp() throws Exception {
    openMocks(this);
    benchmark = new CollectionsDeserializationBenchmark();

    // Inject mock Gson into benchmark instance using reflection
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // Inject json string (non-null) into benchmark instance using reflection
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "[{\"intValue\":1,\"longValue\":2,\"stringValue\":\"test\"}]");

    // Access private static final LIST_TYPE_TOKEN via reflection
    Field listTypeTokenField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE_TOKEN");
    listTypeTokenField.setAccessible(true);
    listTypeToken = (TypeToken<?>) listTypeTokenField.get(null);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsDefault_invokesFromJsonCorrectNumberOfTimes() {
    int reps = 5;

    benchmark.timeCollectionsDefault(reps);

    verify(gsonMock, times(reps))
        .fromJson(eq("[{\"intValue\":1,\"longValue\":2,\"stringValue\":\"test\"}]"),
            eq(listTypeToken));
  }

  @Test
    @Timeout(8000)
  void timeCollectionsDefault_zeroReps_noInvocation() {
    int reps = 0;

    benchmark.timeCollectionsDefault(reps);

    verify(gsonMock, times(0))
        .fromJson(org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.any(TypeToken.class));
  }
}