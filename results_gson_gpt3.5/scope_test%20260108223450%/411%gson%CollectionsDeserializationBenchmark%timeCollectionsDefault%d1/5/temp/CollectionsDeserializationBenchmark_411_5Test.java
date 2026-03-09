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
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class CollectionsDeserializationBenchmark_411_5Test {

  @Mock
  private Gson gsonMock;

  private CollectionsDeserializationBenchmark benchmark;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() throws Exception {
    mocks = openMocks(this);
    benchmark = new CollectionsDeserializationBenchmark();

    // Inject gsonMock into private field gson using reflection
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // Inject a non-null json string into private field json using reflection
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "[{\"intValue\":1,\"longValue\":2,\"stringValue\":\"test\"}]");
  }

  @Test
    @Timeout(8000)
  void testTimeCollectionsDefault_zeroReps() {
    benchmark.timeCollectionsDefault(0);
    // Verify no calls to gson.fromJson when reps=0
    verify(gsonMock, times(0)).fromJson(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(TypeToken.class));
  }

  @Test
    @Timeout(8000)
  void testTimeCollectionsDefault_positiveReps() throws Exception {
    int reps = 3;

    // Call the focal method
    benchmark.timeCollectionsDefault(reps);

    // Verify gson.fromJson called exactly reps times with correct arguments
    Field listTypeTokenField = CollectionsDeserializationBenchmark.class.getDeclaredField("LIST_TYPE_TOKEN");
    listTypeTokenField.setAccessible(true);
    TypeToken<?> listTypeToken = (TypeToken<?>) listTypeTokenField.get(null);

    verify(gsonMock, times(reps)).fromJson(eq("[{\"intValue\":1,\"longValue\":2,\"stringValue\":\"test\"}]"), eq(listTypeToken));
  }
}