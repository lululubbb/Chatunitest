package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeWriter_518_2Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() throws IOException {
    writer = new JsonTreeWriter();
    // Initialize the writer with a beginArray() call so that the stack is not empty
    writer.beginArray();
  }

  @Test
    @Timeout(8000)
  public void value_shouldPutJsonPrimitiveAndReturnThis_whenValueIsFiniteAndLenientFalse() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Arrange
    double testValue = 123.456;

    // Act
    JsonWriter result = writer.value(testValue);

    // Assert
    assertSame(writer, result);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Object topElement = peekMethod.invoke(writer);
    assertTrue(topElement instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) topElement;
    assertEquals(testValue, primitive.getAsDouble(), 1e-15);
  }

  @Test
    @Timeout(8000)
  public void value_shouldThrowIllegalArgumentException_whenValueIsNaNAndLenientFalse() {
    double nanValue = Double.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(nanValue);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void value_shouldThrowIllegalArgumentException_whenValueIsPositiveInfinityAndLenientFalse() {
    double posInf = Double.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(posInf);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_shouldThrowIllegalArgumentException_whenValueIsNegativeInfinityAndLenientFalse() {
    double negInf = Double.NEGATIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(negInf);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_shouldAllowNaN_whenLenientTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonTreeWriter spyWriter = spy(new JsonTreeWriter());
    spyWriter.beginArray();
    doReturn(true).when(spyWriter).isLenient();

    double nanValue = Double.NaN;

    JsonWriter result = spyWriter.value(nanValue);
    assertSame(spyWriter, result);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Object topElement = peekMethod.invoke(spyWriter);
    assertTrue(topElement instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) topElement;
    assertTrue(Double.isNaN(primitive.getAsDouble()));
  }

  @Test
    @Timeout(8000)
  public void value_shouldAllowInfinity_whenLenientTrue() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    JsonTreeWriter spyWriter = spy(new JsonTreeWriter());
    spyWriter.beginArray();
    doReturn(true).when(spyWriter).isLenient();

    double posInf = Double.POSITIVE_INFINITY;
    double negInf = Double.NEGATIVE_INFINITY;

    spyWriter.value(posInf);
    JsonWriter resultNeg = spyWriter.value(negInf);
    assertSame(spyWriter, resultNeg);

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Object topElement = peekMethod.invoke(spyWriter);
    assertTrue(topElement instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) topElement;
    assertEquals(negInf, primitive.getAsDouble(), 0d);
  }
}