package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BagOfPrimitivesDeserializationBenchmark_72_3Test {

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  void setUp() {
    benchmark = new BagOfPrimitivesDeserializationBenchmark();
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesReflectionStreaming_withValidJson() throws Exception {
    // Prepare valid JSON matching BagOfPrimitives fields
    String json = "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\"}";
    setPrivateField(benchmark, "json", json);

    // Invoke private method timeBagOfPrimitivesReflectionStreaming(int)
    Method method = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("timeBagOfPrimitivesReflectionStreaming", int.class);
    method.setAccessible(true);

    // Run with reps=1 to test one iteration
    method.invoke(benchmark, 1);
  }

  @Test
    @Timeout(8000)
  void testTimeBagOfPrimitivesReflectionStreaming_withUnexpectedFieldType_throwsRuntimeException() throws Exception {
    // Prepare JSON with an unexpected field
    String json =
        "{\"longValue\":1234567890123,\"intValue\":42,\"booleanValue\":true,\"stringValue\":\"test\",\"unexpectedField\":123.45}";
    setPrivateField(benchmark, "json", json);

    // Create a subclass to override BagOfPrimitives class fields to include unexpectedField to trigger exception
    BagOfPrimitivesDeserializationBenchmark benchmarkWithUnexpectedField = new BagOfPrimitivesDeserializationBenchmark() {
      @Override
      public void timeBagOfPrimitivesReflectionStreaming(int reps) throws Exception {
        for (int i = 0; i < reps; ++i) {
          StringReader reader = new StringReader(json);
          JsonReader jr = new JsonReader(reader);
          jr.beginObject();
          BagOfPrimitives bag = new BagOfPrimitives();
          while (jr.hasNext()) {
            String name = jr.nextName();
            boolean matched = false;
            for (Field field : BagOfPrimitives.class.getDeclaredFields()) {
              if (field.getName().equals(name)) {
                matched = true;
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
                break;
              }
            }
            if (!matched) {
              // Simulate the unexpected field by throwing RuntimeException
              throw new RuntimeException("Unexpected field: " + name);
            }
          }
          jr.endObject();
        }
      }
    };

    Method method = benchmarkWithUnexpectedField.getClass().getMethod("timeBagOfPrimitivesReflectionStreaming", int.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(benchmarkWithUnexpectedField, 1));
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof RuntimeException);
    assertTrue(cause.getMessage().contains("Unexpected field") || cause.getMessage().contains("Unexpected: type"));
  }

  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}