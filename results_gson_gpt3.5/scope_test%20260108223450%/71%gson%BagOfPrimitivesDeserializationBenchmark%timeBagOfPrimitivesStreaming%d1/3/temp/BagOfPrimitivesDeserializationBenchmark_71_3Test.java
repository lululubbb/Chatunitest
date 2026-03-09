package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BagOfPrimitivesDeserializationBenchmark_71_3Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
    // Use reflection to set private field 'json'
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Set valid JSON string with all expected fields
    jsonField.set(benchmark, "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}");
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_validJson_multipleReps() throws IOException {
    // Test with 3 repetitions
    benchmark.timeBagOfPrimitivesStreaming(3);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_zeroReps() throws IOException {
    // Test with 0 repetitions, should do nothing and not throw
    benchmark.timeBagOfPrimitivesStreaming(0);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesStreaming_unexpectedName_throwsIOException() throws Exception {
    // Set json with unexpected field name to trigger IOException
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"unexpectedField\":1}");

    IOException thrown = assertThrows(IOException.class, () -> {
      benchmark.timeBagOfPrimitivesStreaming(1);
    });
    // Optionally assert message contains "Unexpected name"
    assert(thrown.getMessage().contains("Unexpected name"));
  }
}