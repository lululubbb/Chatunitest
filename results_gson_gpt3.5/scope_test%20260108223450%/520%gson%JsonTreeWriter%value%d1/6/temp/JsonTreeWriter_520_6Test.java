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
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeWriter_520_6Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void value_nullValue_returnsNullValue() throws IOException {
    JsonWriter result = writer.value((Number) null);
    assertSame(writer.nullValue(), result);
  }

  @Test
    @Timeout(8000)
  public void value_lenientFalse_nan_throwsIllegalArgumentException() throws Exception {
    // Use reflection to set lenient to false
    Method isLenientMethod = JsonWriter.class.getDeclaredMethod("isLenient");
    isLenientMethod.setAccessible(true);

    // Spy on writer to mock isLenient() to return false
    JsonTreeWriter spyWriter = spy(writer);
    doReturn(false).when(spyWriter).isLenient();

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_lenientFalse_infinity_throwsIllegalArgumentException() throws Exception {
    JsonTreeWriter spyWriter = spy(writer);
    doReturn(false).when(spyWriter).isLenient();

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("NaN and infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_lenientTrue_acceptsNaNAndInfinity() throws IOException {
    JsonTreeWriter spyWriter = spy(writer);
    doReturn(true).when(spyWriter).isLenient();

    // NaN and Infinity should be accepted without exception
    JsonWriter resultNaN = spyWriter.value(Double.NaN);
    assertSame(spyWriter, resultNaN);

    JsonWriter resultInf = spyWriter.value(Double.POSITIVE_INFINITY);
    assertSame(spyWriter, resultInf);
  }

  @Test
    @Timeout(8000)
  public void value_validNumber_putCalledAndReturnsThis() throws Exception {
    JsonTreeWriter spyWriter = spy(writer);
    doReturn(true).when(spyWriter).isLenient();

    Number number = 42;
    JsonWriter result = spyWriter.value(number);

    // verify put was called with JsonPrimitive(number)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", com.google.gson.JsonElement.class);
    putMethod.setAccessible(true);

    // We cannot verify private method call via Mockito easily, but spy verifies public calls.
    // Instead, verify result is spyWriter itself and no exception thrown
    assertSame(spyWriter, result);
  }
}