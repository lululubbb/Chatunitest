package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.GsonBuilder;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class SerializationBenchmark_635_6Test {

  private SerializationBenchmark serializationBenchmark;
  private Gson gsonMock;
  private BagOfPrimitives bagMock;

  @BeforeEach
  public void setUp() throws Exception {
    serializationBenchmark = new SerializationBenchmark();

    gsonMock = Mockito.mock(Gson.class);
    bagMock = Mockito.mock(BagOfPrimitives.class);

    // Inject mocks into private fields using reflection
    Field gsonField = SerializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(serializationBenchmark, gsonMock);

    Field bagField = SerializationBenchmark.class.getDeclaredField("bag");
    bagField.setAccessible(true);
    bagField.set(serializationBenchmark, bagMock);
  }

  @Test
    @Timeout(8000)
  public void testTimeObjectSerialization_zeroReps() {
    serializationBenchmark.timeObjectSerialization(0);
    // Verify gson.toJson is never called
    verify(gsonMock, times(0)).toJson(any());
  }

  @Test
    @Timeout(8000)
  public void testTimeObjectSerialization_positiveReps() {
    int reps = 5;
    serializationBenchmark.timeObjectSerialization(reps);
    // Verify gson.toJson is called exactly reps times with bag
    verify(gsonMock, times(reps)).toJson(bagMock);
  }

  @Test
    @Timeout(8000)
  public void testTimeObjectSerialization_oneRep() {
    serializationBenchmark.timeObjectSerialization(1);
    verify(gsonMock, times(1)).toJson(bagMock);
  }
}