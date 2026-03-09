package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionsDeserializationBenchmark_412_1Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();
    // Set the private json field with valid JSON array string for BagOfPrimitives objects
    String json = "[" +
        "{\"longValue\":123,\"intValue\":456,\"booleanValue\":true,\"stringValue\":\"abc\"}," +
        "{\"longValue\":789,\"intValue\":0,\"booleanValue\":false,\"stringValue\":\"def\"}" +
        "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsStreaming_validJson_runsWithoutException() throws Exception {
    // Run with 2 repetitions
    benchmark.timeCollectionsStreaming(2);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsStreaming_zeroReps_runsWithoutException() throws Exception {
    benchmark.timeCollectionsStreaming(0);
  }

  @Test
    @Timeout(8000)
  void timeCollectionsStreaming_unexpectedName_throwsIOException() throws Exception {
    // Set json with an unexpected field name to trigger IOException
    String jsonWithUnexpectedName = "[" +
        "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"x\",\"unexpected\":5}" +
        "]";
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, jsonWithUnexpectedName);

    assertThrows(IOException.class, () -> benchmark.timeCollectionsStreaming(1));
  }
}