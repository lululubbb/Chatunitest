package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JsonArray_668_4Test {

  private JsonArray jsonArray;
  private ArrayList<JsonElement> mockElements;

  @BeforeEach
  public void setUp() throws Exception {
    jsonArray = new JsonArray();

    // Create a real ArrayList<JsonElement> mockElements
    mockElements = new ArrayList<>();

    // Use reflection to set the private final field 'elements' to the mockElements
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    elementsField.set(jsonArray, mockElements);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_returnsElementsHashCode() {
    int expectedHashCode = 123456789;

    // Replace the elements list with a custom ArrayList that returns expectedHashCode for hashCode
    ArrayList<JsonElement> customElements = new ArrayList<JsonElement>() {
      @Override
      public int hashCode() {
        return expectedHashCode;
      }
    };

    try {
      Field elementsField = JsonArray.class.getDeclaredField("elements");
      elementsField.setAccessible(true);
      elementsField.set(jsonArray, customElements);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    int actualHashCode = jsonArray.hashCode();

    assertEquals(expectedHashCode, actualHashCode);
  }
}