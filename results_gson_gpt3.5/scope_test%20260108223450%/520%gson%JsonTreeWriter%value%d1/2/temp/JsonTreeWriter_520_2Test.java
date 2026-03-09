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
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_520_2Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void value_nullValue_callsNullValue() throws Exception {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(true).when(spyWriter).isLenient();
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void value_validNumber_putsJsonPrimitiveAndReturnsThis() throws Exception {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(true).when(spyWriter).isLenient();

    Number number = 123;
    JsonWriter returned = spyWriter.value(number);

    // Verify put called with JsonPrimitive wrapping the number
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", com.google.gson.JsonElement.class);
    putMethod.setAccessible(true);
    // We cannot directly verify private method call with spy, so we verify effect on field "product"
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object productValue = productField.get(spyWriter);
    assertTrue(productValue instanceof JsonPrimitive);
    assertEquals(number, ((JsonPrimitive) productValue).getAsNumber());

    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void value_nonLenientWithNaN_throwsIllegalArgumentException() {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(false).when(spyWriter).isLenient();

    Number nan = Double.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(nan);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void value_nonLenientWithPositiveInfinity_throwsIllegalArgumentException() {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(false).when(spyWriter).isLenient();

    Number posInf = Double.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(posInf);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_nonLenientWithNegativeInfinity_throwsIllegalArgumentException() {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(false).when(spyWriter).isLenient();

    Number negInf = Double.NEGATIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      spyWriter.value(negInf);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }
}