package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

class JsonElement_578_5Test {

  @Test
    @Timeout(8000)
  void testGetAsJsonArray_whenIsJsonArrayTrue_returnsThisAsJsonArray() {
    JsonArray spyElement = spy(JsonArray.class);
    doReturn(true).when(spyElement).isJsonArray();

    JsonArray result = spyElement.getAsJsonArray();

    assertSame(spyElement, result);
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonArray_whenIsJsonArrayFalse_throwsIllegalStateException() {
    JsonElement element = spy(JsonElement.class);
    doReturn(false).when(element).isJsonArray();
    doReturn("fakeJsonElement").when(element).toString();

    IllegalStateException exception = assertThrows(IllegalStateException.class, element::getAsJsonArray);
    assertEquals("Not a JSON Array: fakeJsonElement", exception.getMessage());
  }
}