package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.GsonBuilder;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SerializationBenchmark_635_4Test {

  private SerializationBenchmark serializationBenchmark;

  @Mock
  private Gson gsonMock;

  @BeforeEach
  void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    serializationBenchmark = new SerializationBenchmark();

    // Use reflection to set private fields gson and bag
    java.lang.reflect.Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(serializationBenchmark, gsonMock);

    java.lang.reflect.Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    bagField.set(serializationBenchmark, new BagOfPrimitives());

    // Set pretty field as well (even if not used here)
    java.lang.reflect.Field prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(serializationBenchmark, false);
  }

  @Test
    @Timeout(8000)
  void testTimeObjectSerialization_zeroReps() {
    serializationBenchmark.timeObjectSerialization(0);
    verify(gsonMock, never()).toJson(any());
  }

  @Test
    @Timeout(8000)
  void testTimeObjectSerialization_multipleReps() {
    int reps = 5;
    serializationBenchmark.timeObjectSerialization(reps);
    verify(gsonMock, times(reps)).toJson(any(BagOfPrimitives.class));
  }
}

// Dummy BagOfPrimitives class for compilation
class BagOfPrimitives {}