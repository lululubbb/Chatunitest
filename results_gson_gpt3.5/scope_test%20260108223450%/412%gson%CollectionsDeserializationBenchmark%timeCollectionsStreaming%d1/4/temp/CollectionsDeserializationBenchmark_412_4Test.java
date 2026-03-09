package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CollectionsDeserializationBenchmark_412_4Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();
    // Use reflection to set private field 'json'
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Prepare a valid JSON array of BagOfPrimitives objects
    String json = "[" +
        "{\"longValue\":123,\"intValue\":10,\"booleanValue\":true,\"stringValue\":\"test1\"}," +
        "{\"longValue\":456,\"intValue\":20,\"booleanValue\":false,\"stringValue\":\"test2\"}" +
        "]";
    jsonField.set(benchmark, json);
    // Set gson field to a mock or null since not used in the focal method
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, null);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_zeroReps() throws IOException {
    benchmark.timeCollectionsStreaming(0); // Should complete without exceptions
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_multipleReps() throws IOException {
    benchmark.timeCollectionsStreaming(3); // Should parse JSON 3 times without exceptions
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_unexpectedName_throwsIOException() throws Exception {
    // Set json with an unexpected field name to trigger IOException
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String badJson = "[" +
        "{\"longValue\":123,\"intValue\":10,\"booleanValue\":true,\"stringValue\":\"test1\",\"unexpectedField\":5}" +
        "]";
    jsonField.set(benchmark, badJson);

    assertThrows(IOException.class, () -> benchmark.timeCollectionsStreaming(1));
  }
}