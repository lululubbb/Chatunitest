package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class JsonWriter_392_4Test {

  private Writer mockWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    mockWriter = mock(Writer.class);
    jsonWriter = new JsonWriter(mockWriter);
  }

  private void setLenient(JsonWriter writer, boolean value) throws Exception {
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.set(writer, value);
  }

  @Test
    @Timeout(8000)
  public void value_writesFiniteFloat_appendsFloatString() throws Exception {
    // Arrange
    float testValue = 123.45f;

    JsonWriter spyWriter = spy(jsonWriter);

    // Set lenient to false explicitly to ensure exception on NaN/Infinite
    setLenient(spyWriter, false);

    // Act
    JsonWriter returned = spyWriter.value(testValue);

    // Assert
    assertSame(spyWriter, returned);

    InOrder inOrder = inOrder(spyWriter, mockWriter);
    inOrder.verify(mockWriter).append(Float.toString(testValue));
  }

  @Test
    @Timeout(8000)
  public void value_nanFloat_throwsIllegalArgumentException() throws Exception {
    float nanValue = Float.NaN;

    JsonWriter spyWriter = spy(jsonWriter);
    setLenient(spyWriter, false);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(nanValue);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_infiniteFloat_throwsIllegalArgumentException() throws Exception {
    float infiniteValue = Float.POSITIVE_INFINITY;

    JsonWriter spyWriter = spy(jsonWriter);
    setLenient(spyWriter, false);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(infiniteValue);
    });
    assertTrue(thrown.getMessage().contains("Numeric values must be finite"));
  }

  @Test
    @Timeout(8000)
  public void value_lenientAllowsNaNAndInfinite() throws Exception {
    float nanValue = Float.NaN;
    float infiniteValue = Float.NEGATIVE_INFINITY;

    JsonWriter spyWriter = spy(jsonWriter);
    setLenient(spyWriter, true);

    // Act & Assert no exception for NaN
    JsonWriter returnedNaN = spyWriter.value(nanValue);
    assertSame(spyWriter, returnedNaN);

    // Reset mocks
    clearInvocations(spyWriter, mockWriter);

    // Act & Assert no exception for infinite
    JsonWriter returnedInfinite = spyWriter.value(infiniteValue);
    assertSame(spyWriter, returnedInfinite);
  }
}