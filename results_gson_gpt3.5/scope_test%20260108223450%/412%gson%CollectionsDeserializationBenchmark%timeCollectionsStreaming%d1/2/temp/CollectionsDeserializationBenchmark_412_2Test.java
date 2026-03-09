package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CollectionsDeserializationBenchmark_412_2Test {

  private CollectionsDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    benchmark = new CollectionsDeserializationBenchmark();
    // Set the private field 'json' via reflection
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    // Provide a JSON string representing an array of BagOfPrimitives objects
    // Example with two objects, one with all fields, one with empty stringValue
    String json = "[" +
        "{" +
        "\"longValue\":1234567890123," +
        "\"intValue\":42," +
        "\"booleanValue\":true," +
        "\"stringValue\":\"testString\"" +
        "}," +
        "{" +
        "\"longValue\":0," +
        "\"intValue\":0," +
        "\"booleanValue\":false," +
        "\"stringValue\":\"\"" +
        "}" +
        "]";
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_validJson_runsWithoutException() throws Exception {
    // Invoke private method timeCollectionsStreaming with reps=1
    Method method = CollectionsDeserializationBenchmark.class.getDeclaredMethod("timeCollectionsStreaming", int.class);
    method.setAccessible(true);
    method.invoke(benchmark, 1);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_multipleReps_runsWithoutException() throws Exception {
    Method method = CollectionsDeserializationBenchmark.class.getDeclaredMethod("timeCollectionsStreaming", int.class);
    method.setAccessible(true);
    method.invoke(benchmark, 3);
  }

  @Test
    @Timeout(8000)
  public void testTimeCollectionsStreaming_unexpectedName_throwsIOException() throws Exception {
    // Set json with an unexpected field name
    Field jsonField = CollectionsDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    String invalidJson = "[" +
        "{" +
        "\"longValue\":123," +
        "\"intValue\":1," +
        "\"booleanValue\":true," +
        "\"stringValue\":\"valid\"," +
        "\"unexpectedField\":999" +
        "}" +
        "]";
    jsonField.set(benchmark, invalidJson);

    Method method = CollectionsDeserializationBenchmark.class.getDeclaredMethod("timeCollectionsStreaming", int.class);
    method.setAccessible(true);

    // Expect IOException due to unexpected field name; unwrap InvocationTargetException
    assertThrows(IOException.class, () -> {
      try {
        method.invoke(benchmark, 1);
      } catch (InvocationTargetException e) {
        // Rethrow the underlying cause if it's IOException, else rethrow the InvocationTargetException
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw cause;
        }
        throw e;
      }
    });
  }

}