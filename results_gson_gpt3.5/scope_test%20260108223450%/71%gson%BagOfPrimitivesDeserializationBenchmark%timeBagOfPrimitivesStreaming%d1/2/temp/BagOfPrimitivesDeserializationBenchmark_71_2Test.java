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

public class BagOfPrimitivesDeserializationBenchmark_71_2Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
    // Using reflection to set the private field json
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Set a valid JSON string for the test
    jsonField.set(benchmark, "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"testString\"}");
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_ValidInput() throws IOException {
    // Test with reps = 1
    benchmark.timeBagOfPrimitivesStreaming(1);
    // Test with reps > 1
    benchmark.timeBagOfPrimitivesStreaming(3);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_UnexpectedName_ThrowsIOException() throws Exception {
    // Set json with an unexpected field name to cause IOException
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"unknownName\":123}");

    IOException thrown = assertThrows(IOException.class, () -> {
      benchmark.timeBagOfPrimitivesStreaming(1);
    });
    assertTrue(thrown.getMessage().contains("Unexpected name"));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_EmptyObject() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Set json to empty JSON object
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{}");

    benchmark.timeBagOfPrimitivesStreaming(1);
  }
}