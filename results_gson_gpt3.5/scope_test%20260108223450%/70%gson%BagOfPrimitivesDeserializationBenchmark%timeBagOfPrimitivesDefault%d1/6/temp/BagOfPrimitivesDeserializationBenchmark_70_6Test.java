package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BagOfPrimitivesDeserializationBenchmark_70_6Test {

  @Mock
  private Gson gsonMock;

  private BagOfPrimitivesDeserializationBenchmark benchmark;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);
    benchmark = new BagOfPrimitivesDeserializationBenchmark();

    // Inject mocked Gson instance into benchmark using reflection
    Field gsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("gson");
    gsonField.setAccessible(true);
    gsonField.set(benchmark, gsonMock);

    // Inject json string field with dummy json
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"a\":1,\"b\":true,\"c\":\"text\"}");

    // If needed, invoke private or package-private setUp method annotated with @BeforeExperiment
    Method setUpMethod = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Setup default behavior for gsonMock.fromJson to avoid NullPointerException if needed
    when(gsonMock.fromJson(anyString(), eq(BagOfPrimitives.class))).thenReturn(null);
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_zeroReps() {
    benchmark.timeBagOfPrimitivesDefault(0);
    verify(gsonMock, never()).fromJson(anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_oneRep() throws Exception {
    // Ensure json field is set to non-null value
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"a\":1,\"b\":true,\"c\":\"text\"}");

    // Reset interactions before test
    clearInvocations(gsonMock);

    // Setup default behavior for gsonMock.fromJson(anyString(), ...)
    when(gsonMock.fromJson(anyString(), eq(BagOfPrimitives.class))).thenReturn(null);

    benchmark.timeBagOfPrimitivesDefault(1);
    verify(gsonMock, times(1)).fromJson(anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_multipleReps() throws Exception {
    // Ensure json field is set to non-null value
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, "{\"a\":1,\"b\":true,\"c\":\"text\"}");

    // Reset interactions before test
    clearInvocations(gsonMock);

    // Setup default behavior for gsonMock.fromJson(anyString(), ...)
    when(gsonMock.fromJson(anyString(), eq(BagOfPrimitives.class))).thenReturn(null);

    int reps = 5;
    benchmark.timeBagOfPrimitivesDefault(reps);
    verify(gsonMock, times(reps)).fromJson(anyString(), eq(BagOfPrimitives.class));
  }

  @Test
    @Timeout(8000)
  public void testTimeBagOfPrimitivesDefault_nullJson() throws Exception {
    // Set json field to null to test behavior
    Field jsonField = BagOfPrimitivesDeserializationBenchmark.class.getDeclaredField("json");
    jsonField.setAccessible(true);
    jsonField.set(benchmark, null);

    // Reset interactions before test
    clearInvocations(gsonMock);

    // Setup behavior for null json input
    when(gsonMock.fromJson(isNull(String.class), eq(BagOfPrimitives.class))).thenReturn(null);

    benchmark.timeBagOfPrimitivesDefault(3);
    verify(gsonMock, times(3)).fromJson(isNull(String.class), eq(BagOfPrimitives.class));
  }
}