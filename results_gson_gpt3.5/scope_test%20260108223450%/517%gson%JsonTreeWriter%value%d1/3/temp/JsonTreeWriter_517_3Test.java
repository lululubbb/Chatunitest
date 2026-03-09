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
import java.lang.reflect.Method;

class JsonTreeWriter_517_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void value_validFloat_returnsThis() throws Exception {
    // Use reflection to set lenient to true to allow all float values
    Method isLenientMethod = JsonWriter.class.getDeclaredMethod("isLenient");
    isLenientMethod.setAccessible(true);

    // Spy on jsonTreeWriter to mock isLenient() method
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(true).when(spyWriter).isLenient();

    JsonWriter returned = spyWriter.value(1.23f);
    assertSame(spyWriter, returned);

    // Use reflection to check that the JsonPrimitive was put in the stack or product
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    Object product = getMethod.invoke(spyWriter);
    assertNotNull(product);
    assertTrue(product instanceof com.google.gson.JsonPrimitive);
    assertEquals(1.23f, ((JsonPrimitive) product).getAsFloat());
  }

  @Test
    @Timeout(8000)
  void value_nanFloat_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Float.NaN);
    });
    assertTrue(exception.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  void value_infiniteFloat_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Float.POSITIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  void value_negativeInfiniteFloat_throwsIllegalArgumentException() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Float.NEGATIVE_INFINITY);
    });
    assertTrue(exception.getMessage().contains("infinities"));
  }
}