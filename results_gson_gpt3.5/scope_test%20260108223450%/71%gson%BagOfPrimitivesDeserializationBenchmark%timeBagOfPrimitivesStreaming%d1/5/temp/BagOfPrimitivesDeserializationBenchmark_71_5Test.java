package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

public class BagOfPrimitivesDeserializationBenchmark_71_5Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
    // Use reflection to set the private field 'json' to a valid JSON string for testing
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String json = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"testString\"}";
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_withValidReps() throws IOException {
    int reps = 5;
    // Just call the method, no exception expected
    benchmark.timeBagOfPrimitivesStreaming(reps);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_zeroReps() throws IOException {
    int reps = 0;
    // Should do nothing without throwing
    benchmark.timeBagOfPrimitivesStreaming(reps);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_unexpectedName_throwsIOException() throws Exception {
    // Set json field to JSON with an unexpected property name
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String json = "{\"unexpectedName\":123}";
    jsonField.set(benchmark, json);

    IOException thrown = assertThrows(IOException.class, () -> benchmark.timeBagOfPrimitivesStreaming(1));
    assertTrue(thrown.getMessage().contains("Unexpected name: unexpectedName"));
  }
}