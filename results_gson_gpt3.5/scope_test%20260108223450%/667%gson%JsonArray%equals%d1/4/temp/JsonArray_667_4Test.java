package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.lang.reflect.Field;

class JsonArrayEqualsTest {

  // Helper to create a JsonArray with given elements
  private JsonArray createJsonArrayWithElements(JsonElement... elements) throws Exception {
    JsonArray jsonArray = new JsonArray();
    // Use reflection to access private final field "elements"
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> internalList = (ArrayList<JsonElement>) elementsField.get(jsonArray);
    internalList.clear(); // clear any existing elements
    for (JsonElement elem : elements) {
      internalList.add(elem);
    }
    return jsonArray;
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameReference() {
    JsonArray jsonArray = new JsonArray();
    assertTrue(jsonArray.equals(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void testEquals_null() {
    JsonArray jsonArray = new JsonArray();
    assertFalse(jsonArray.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentClass() {
    JsonArray jsonArray = new JsonArray();
    String other = "not a JsonArray";
    assertFalse(jsonArray.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_emptyArrays() throws Exception {
    JsonArray jsonArray1 = createJsonArrayWithElements();
    JsonArray jsonArray2 = createJsonArrayWithElements();
    assertTrue(jsonArray1.equals(jsonArray2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameElements() throws Exception {
    JsonElement elem1 = Mockito.mock(JsonElement.class);
    JsonElement elem2 = Mockito.mock(JsonElement.class);

    // Mock equals to behave correctly for elem1 and elem2
    Mockito.when(elem1.equals(Mockito.any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == elem1;
    });
    Mockito.when(elem2.equals(Mockito.any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == elem2;
    });

    // Also mock hashCode to avoid possible issues in collections
    Mockito.when(elem1.hashCode()).thenReturn(1);
    Mockito.when(elem2.hashCode()).thenReturn(2);

    JsonArray jsonArray1 = createJsonArrayWithElements(elem1, elem2);
    JsonArray jsonArray2 = createJsonArrayWithElements(elem1, elem2);
    assertTrue(jsonArray1.equals(jsonArray2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentElements() throws Exception {
    JsonElement elem1 = Mockito.mock(JsonElement.class);
    JsonElement elem2 = Mockito.mock(JsonElement.class);
    JsonElement elem3 = Mockito.mock(JsonElement.class);

    // Mock equals behavior
    Mockito.when(elem1.equals(Mockito.any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == elem1;
    });
    Mockito.when(elem2.equals(Mockito.any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == elem2;
    });
    Mockito.when(elem3.equals(Mockito.any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == elem3;
    });

    // Also mock hashCode to avoid possible issues in collections
    Mockito.when(elem1.hashCode()).thenReturn(1);
    Mockito.when(elem2.hashCode()).thenReturn(2);
    Mockito.when(elem3.hashCode()).thenReturn(3);

    JsonArray jsonArray1 = createJsonArrayWithElements(elem1, elem2);
    JsonArray jsonArray2 = createJsonArrayWithElements(elem1, elem3);
    assertFalse(jsonArray1.equals(jsonArray2));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentSize() throws Exception {
    JsonElement elem1 = Mockito.mock(JsonElement.class);
    Mockito.when(elem1.equals(Mockito.any())).thenAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return arg == elem1;
    });
    Mockito.when(elem1.hashCode()).thenReturn(1);

    JsonArray jsonArray1 = createJsonArrayWithElements(elem1);
    JsonArray jsonArray2 = createJsonArrayWithElements();
    assertFalse(jsonArray1.equals(jsonArray2));
  }
}