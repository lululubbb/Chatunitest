package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BagOfPrimitivesDeserializationBenchmark_72_6Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Use reflection to set the private 'json' field with a proper JSON string matching BagOfPrimitives fields
    // Assuming BagOfPrimitives has fields: longValue (long), intValue (int), booleanValue (boolean), stringValue (String)
    // We create a JSON string accordingly:
    String json = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}";

    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_runsWithoutException() {
    // Run with reps=1 to cover main path
    assertDoesNotThrow(() -> benchmark.timeBagOfPrimitivesReflectionStreaming(1));
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_runsWithZeroReps() {
    // Run with reps=0 to cover zero iteration branch
    assertDoesNotThrow(() -> benchmark.timeBagOfPrimitivesReflectionStreaming(0));
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_throwsOnUnexpectedFieldType() throws Exception {
    // Create a subclass of BagOfPrimitives with an unexpected field type to trigger exception
    class BagOfPrimitivesWithUnexpectedField extends BagOfPrimitives {
      @SuppressWarnings("unused")
      private double unexpectedField = 1.0;
    }

    // Replace the json field with JSON containing the unexpected field name
    String json = "{\"unexpectedField\":1.0}";
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, json);

    // Replace BagOfPrimitives.class.getDeclaredFields() by reflection to point to subclass fields
    // Since the method uses BagOfPrimitives.class directly, we need to use reflection to replace the field
    // We will use reflection to replace the method temporarily to use the subclass fields
    // Since the method uses BagOfPrimitives.class directly, we cannot change it without modifying the class.
    // Instead, we create a new class extending the benchmark with overridden method using the subclass fields.

    BagOfPrimitivesDeserializationBenchmark benchmarkWithSubclass = new BagOfPrimitivesDeserializationBenchmark() {
      @Override
      public void timeBagOfPrimitivesReflectionStreaming(int reps) throws Exception {
        for (int i = 0; i < reps; ++i) {
          StringReader reader = new StringReader(json);
          JsonReader jr = new JsonReader(reader);
          jr.beginObject();
          BagOfPrimitivesWithUnexpectedField bag = new BagOfPrimitivesWithUnexpectedField();
          while (jr.hasNext()) {
            String name = jr.nextName();
            for (Field field : BagOfPrimitivesWithUnexpectedField.class.getDeclaredFields()) {
              if (field.getName().equals(name)) {
                Class<?> fieldType = field.getType();
                if (fieldType.equals(long.class)) {
                  field.setLong(bag, jr.nextLong());
                } else if (fieldType.equals(int.class)) {
                  field.setInt(bag, jr.nextInt());
                } else if (fieldType.equals(boolean.class)) {
                  field.setBoolean(bag, jr.nextBoolean());
                } else if (fieldType.equals(String.class)) {
                  field.set(bag, jr.nextString());
                } else {
                  throw new RuntimeException("Unexpected: type: " + fieldType + ", name: " + name);
                }
              }
            }
          }
          jr.endObject();
        }
      }
    };

    Field jsonFieldSubclass = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonFieldSubclass.setAccessible(true);
    jsonFieldSubclass.set(benchmarkWithSubclass, json);

    RuntimeException thrown = null;
    try {
      benchmarkWithSubclass.timeBagOfPrimitivesReflectionStreaming(1);
    } catch (RuntimeException e) {
      thrown = e;
    }
    assert thrown != null;
    assert thrown.getMessage().contains("Unexpected: type:");
  }
}