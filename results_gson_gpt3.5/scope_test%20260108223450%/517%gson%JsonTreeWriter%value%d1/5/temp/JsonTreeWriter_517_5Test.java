package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeWriter_517_5Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withNormalFloat_returnsThis() throws IOException {
    JsonWriter result = writer.value(1.5f);
    assertSame(writer, result);
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Float.NaN);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Float.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Float.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_callsPutWithJsonPrimitive() throws Exception {
    // Create a spy on the existing writer and intercept the private put method via reflection
    JsonTreeWriter spyWriter = spy(writer);

    // Use reflection to get the private put method
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // We will create a spy that intercepts calls to put by wrapping the method call
    // But Mockito cannot spy private methods directly, so we use doAnswer on spyWriter.value(float)
    // Instead, we verify that after calling value(2.5f), the internal state contains the expected JsonPrimitive

    spyWriter.value(2.5f);

    // Access the private 'product' field which holds the JsonElement
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(spyWriter);

    assertNotNull(product);
    assertTrue(product instanceof JsonPrimitive);
    assertEquals(2.5f, ((JsonPrimitive) product).getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testValue_lenientAllowsNaN() throws Exception {
    // Create a proxy JsonTreeWriter that returns true for isLenient() by reflection
    // Since JsonTreeWriter is final, we cannot subclass or override methods
    // Instead, use reflection to set the isLenient field or simulate lenient mode

    // Unfortunately, JsonTreeWriter does not have an isLenient field, but inherits isLenient() from JsonWriter

    // Use reflection to create a proxy JsonWriter with lenient set to true
    // JsonWriter has a protected boolean 'lenient' field

    // Create a new JsonTreeWriter instance
    JsonTreeWriter lenientWriter = new JsonTreeWriter();

    // Set the protected 'lenient' field in JsonWriter superclass to true
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.set(lenientWriter, true);

    JsonWriter result = lenientWriter.value(Float.NaN);
    assertSame(lenientWriter, result);
  }
}