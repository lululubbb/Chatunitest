package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

class JsonArray_642_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void add_nullString_shouldAddJsonNullInstance() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    Method addStringMethod = JsonArray.class.getMethod("add", String.class);
    addStringMethod.invoke(jsonArray, (String) null);

    // Using reflection to access private field elements
    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertSame(JsonNull.INSTANCE, elements.get(0));
  }

  @Test
    @Timeout(8000)
  void add_nonNullString_shouldAddJsonPrimitiveWithString() throws NoSuchFieldException, IllegalAccessException {
    String testString = "test";

    jsonArray.add(testString);

    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    JsonElement element = elements.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(testString, element.getAsString());
  }

  @Test
    @Timeout(8000)
  void add_multipleStrings_shouldAddAll() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    Method addStringMethod = JsonArray.class.getMethod("add", String.class);
    addStringMethod.invoke(jsonArray, "a");
    addStringMethod.invoke(jsonArray, (String) null);
    addStringMethod.invoke(jsonArray, "b");

    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(3, elements.size());
    assertTrue(elements.get(0) instanceof JsonPrimitive);
    assertEquals("a", elements.get(0).getAsString());
    assertSame(JsonNull.INSTANCE, elements.get(1));
    assertTrue(elements.get(2) instanceof JsonPrimitive);
    assertEquals("b", elements.get(2).getAsString());
  }

  @Test
    @Timeout(8000)
  void invokeAddPrivateMethodUsingReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    Method addMethod = JsonArray.class.getMethod("add", String.class);
    addMethod.invoke(jsonArray, "reflection");

    var elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    ArrayList<JsonElement> elements = (ArrayList<JsonElement>) elementsField.get(jsonArray);

    assertEquals(1, elements.size());
    assertEquals("reflection", elements.get(0).getAsString());
  }
}