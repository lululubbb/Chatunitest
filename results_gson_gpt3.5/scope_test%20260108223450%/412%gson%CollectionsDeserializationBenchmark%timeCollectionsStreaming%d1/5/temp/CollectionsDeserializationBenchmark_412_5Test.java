package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

public class CollectionsDeserializationBenchmark_412_5Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();
    // Use reflection to set the private field 'json' with a valid JSON array string
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "[" +
        "{\"longValue\":1,\"intValue\":2,\"booleanValue\":true,\"stringValue\":\"str1\"}," +
        "{\"longValue\":3,\"intValue\":4,\"booleanValue\":false,\"stringValue\":\"str2\"}" +
        "]");
    // Also set gson field to a mock if needed (not used in focal method)
    Field gsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, null);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_NormalExecution() throws IOException {
    // reps=2 means the loop runs twice
    benchmark.timeCollectionsStreaming(2);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_ZeroReps() throws IOException {
    // reps=0 means loop does not run, should complete normally
    benchmark.timeCollectionsStreaming(0);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_UnexpectedNameThrows() throws Exception {
    // Set json field with JSON containing unexpected field name to trigger IOException
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "[{\"unexpectedField\":123}]");

    IOException thrown = assertThrows(IOException.class, () -> {
      benchmark.timeCollectionsStreaming(1);
    });
    // Check exception message contains "Unexpected name"
    assert(thrown.getMessage().contains("Unexpected name"));
  }

}