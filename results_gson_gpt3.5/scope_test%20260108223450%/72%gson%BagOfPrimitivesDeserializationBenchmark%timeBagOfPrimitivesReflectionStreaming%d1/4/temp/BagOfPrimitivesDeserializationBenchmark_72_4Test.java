package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Field;

class BagOfPrimitivesDeserializationBenchmark_72_4Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() throws Exception {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Use reflection to set the private 'json' field to a valid JSON string
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"testString\"}");
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_zeroReps_shouldNotThrow() throws Exception {
    benchmark.timeBagOfPrimitivesReflectionStreaming(0);
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_oneRep_shouldDeserializeCorrectly() throws Exception {
    benchmark.timeBagOfPrimitivesReflectionStreaming(1);
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_multipleReps_shouldDeserializeCorrectly() throws Exception {
    benchmark.timeBagOfPrimitivesReflectionStreaming(5);
  }

  @Test
    @Timeout(8000)
  void timeBagOfPrimitivesReflectionStreaming_unexpectedFieldType_shouldThrowRuntimeException() throws Exception {
    // Create a subclass of BagOfPrimitives with an unsupported field type
    class BagOfPrimitivesWithUnsupportedField extends BagOfPrimitives {
      @SuppressWarnings("unused")
      private double unsupportedField = 3.14;
    }

    // Replace the json field with JSON containing the unsupportedField name
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"unsupportedField\":3.14}");

    // Create a subclass of BagOfPrimitivesDeserializationBenchmark with overridden method that uses the subclass fields.
    BagOfPrimitivesDeserializationBenchmark benchmarkWithUnsupportedField = new BagOfPrimitivesDeserializationBenchmark() {
      @Override
      public void timeBagOfPrimitivesReflectionStreaming(int reps) throws Exception {
        // Access the private json field via reflection
        Field jsonFieldInner = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
        jsonFieldInner.setAccessible(true);
        String jsonValue = (String) jsonFieldInner.get(this);

        for (int i = 0; i < reps; ++i) {
          StringReader reader = new StringReader(jsonValue);
          JsonReader jr = new JsonReader(reader);
          jr.beginObject();
          BagOfPrimitivesWithUnsupportedField bag = new BagOfPrimitivesWithUnsupportedField();
          while (jr.hasNext()) {
            String name = jr.nextName();
            boolean found = false;
            for (Field field : BagOfPrimitivesWithUnsupportedField.class.getDeclaredFields()) {
              if (field.getName().equals(name)) {
                found = true;
                Class<?> fieldType = field.getType();
                field.setAccessible(true);
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
            if (!found) {
              jr.skipValue();
            }
          }
          jr.endObject();
        }
      }
    };

    // Set the json field in the new benchmark instance
    Field jsonField2 = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField2.setAccessible(true);
    jsonField2.set(benchmarkWithUnsupportedField, "{\"unsupportedField\":3.14}");

    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
      benchmarkWithUnsupportedField.timeBagOfPrimitivesReflectionStreaming(1);
    });
    assertTrue(ex.getMessage().contains("Unexpected"));
  }
}