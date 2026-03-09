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
import java.lang.reflect.Method;

public class JsonTreeWriter_518_5Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void value_validDouble_returnsThis() throws IOException {
    JsonWriter returned = jsonTreeWriter.value(1.23);
    assertSame(jsonTreeWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void value_NaN_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.NaN);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void value_Infinite_throwsIllegalArgumentException() {
    IllegalArgumentException thrownPos = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.POSITIVE_INFINITY);
    });
    assertTrue(thrownPos.getMessage().contains("infinities"));

    IllegalArgumentException thrownNeg = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(Double.NEGATIVE_INFINITY);
    });
    assertTrue(thrownNeg.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void value_allowsNaNIfLenient() throws Exception {
    // Use reflection to set lenient to true since isLenient() is inherited and not shown here
    // We assume isLenient() comes from JsonWriter and defaults to false
    // So we mock JsonTreeWriter to override isLenient() to return true

    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(true).when(spyWriter).isLenient();

    JsonWriter returned = spyWriter.value(Double.NaN);
    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void value_putCalledWithJsonPrimitive() throws Exception {
    // Use reflection to spy on private put method call by spying on JsonTreeWriter
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);

    double testValue = 5.5;
    spyWriter.value(testValue);

    // Capture argument passed to put()
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", com.google.gson.JsonElement.class);
    putMethod.setAccessible(true);
    // We cannot verify private method call with Mockito easily, so verify indirectly:
    // We invoke get() and check that it contains the JsonPrimitive with the value set

    com.google.gson.JsonElement result = spyWriter.get();
    assertTrue(result.isJsonPrimitive());
    JsonPrimitive primitive = result.getAsJsonPrimitive();
    assertTrue(primitive.isNumber());
    assertEquals(testValue, primitive.getAsDouble(), 0.0);
  }
}