package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Iterator;

public class JsonArray_637_5Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() {
    jsonArray = new JsonArray(5);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithCapacity() {
    JsonArray array = new JsonArray(10);
    assertNotNull(array);
    assertEquals(0, array.size());
  }

  @Test
    @Timeout(8000)
  public void testAddAndSize() {
    jsonArray.add(Boolean.TRUE);
    jsonArray.add(Character.valueOf('a'));
    jsonArray.add(123);
    jsonArray.add("string");
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);

    assertEquals(5, jsonArray.size());
    assertTrue(jsonArray.contains(element));
  }

  @Test
    @Timeout(8000)
  public void testAddAll() {
    JsonArray other = new JsonArray();
    other.add("one");
    other.add("two");

    jsonArray.addAll(other);
    assertEquals(2, jsonArray.size());
    assertEquals("one", jsonArray.get(0).getAsString());
    assertEquals("two", jsonArray.get(1).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testSetAndGet() {
    jsonArray.add("zero");
    JsonElement newElement = mock(JsonElement.class);
    when(newElement.getAsString()).thenReturn("new");

    JsonElement old = jsonArray.set(0, newElement);
    assertEquals("zero", old.getAsString());
    assertEquals(newElement, jsonArray.get(0));
  }

  @Test
    @Timeout(8000)
  public void testRemoveByElement() {
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);
    assertTrue(jsonArray.remove(element));
    assertFalse(jsonArray.contains(element));
  }

  @Test
    @Timeout(8000)
  public void testRemoveByIndex() {
    jsonArray.add("a");
    jsonArray.add("b");
    JsonElement removed = jsonArray.remove(0);
    assertEquals("a", removed.getAsString());
    assertEquals(1, jsonArray.size());
  }

  @Test
    @Timeout(8000)
  public void testContains() {
    JsonElement element = mock(JsonElement.class);
    jsonArray.add(element);
    assertTrue(jsonArray.contains(element));
    JsonElement other = mock(JsonElement.class);
    assertFalse(jsonArray.contains(other));
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty() {
    assertTrue(jsonArray.isEmpty());
    jsonArray.add("not empty");
    assertFalse(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIterator() {
    jsonArray.add("a");
    jsonArray.add("b");
    Iterator<JsonElement> it = jsonArray.iterator();
    assertTrue(it.hasNext());
    assertEquals("a", it.next().getAsString());
    assertTrue(it.hasNext());
    assertEquals("b", it.next().getAsString());
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testGet() {
    jsonArray.add("value");
    assertEquals("value", jsonArray.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    jsonArray.add("copy");
    JsonArray copy = jsonArray.deepCopy();
    assertNotSame(jsonArray, copy);
    assertEquals(jsonArray.size(), copy.size());
    assertEquals(jsonArray.get(0).getAsString(), copy.get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElement_Reflection() throws Exception {
    jsonArray.add("single");
    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement single = (JsonElement) method.invoke(jsonArray);
    assertEquals("single", single.getAsString());

    // Remove elements one by one instead of clear()
    while (jsonArray.size() > 0) {
      jsonArray.remove(0);
    }
    jsonArray.add("second");
    JsonElement singleAfterAdd = (JsonElement) method.invoke(jsonArray);
    assertEquals("second", singleAfterAdd.getAsString());

    while (jsonArray.size() > 0) {
      jsonArray.remove(0);
    }
    jsonArray.add("third");

    // Remove only one element here to keep size 1 before adding the fourth
    // This avoids having size 2 when invoking getAsSingleElement
    // So first add "third", then add "fourth" only after removing one element
    // But since we want to test the case with size 2, we need to invoke after adding both

    jsonArray.add("fourth");
    // The method should throw IllegalStateException for size > 1
    // So we catch the exception and assert null on the returned value if no exception

    JsonElement singleAfterAddSecond = null;
    try {
      singleAfterAddSecond = (JsonElement) method.invoke(jsonArray);
      fail("Expected IllegalStateException due to size > 1");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof IllegalStateException);
      assertEquals("Array must have size 1, but has size 2", e.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber() {
    jsonArray.add(123);
    assertEquals(123, jsonArray.getAsNumber().intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString() {
    jsonArray.add("test");
    assertEquals("test", jsonArray.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble() {
    jsonArray.add(1.23);
    assertEquals(1.23, jsonArray.getAsDouble(), 0.0001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal() {
    jsonArray.add("123.45");
    assertEquals("123.45", jsonArray.getAsBigDecimal().toString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger() {
    jsonArray.add("123");
    assertEquals("123", jsonArray.getAsBigInteger().toString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat() {
    jsonArray.add(1.23f);
    assertEquals(1.23f, jsonArray.getAsFloat(), 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong() {
    jsonArray.add(123L);
    assertEquals(123L, jsonArray.getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt() {
    jsonArray.add(123);
    assertEquals(123, jsonArray.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte() {
    jsonArray.add((byte) 1);
    assertEquals(1, jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter() {
    jsonArray.add('a');
    assertEquals('a', jsonArray.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort() {
    jsonArray.add((short) 2);
    assertEquals(2, jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean() {
    jsonArray.add(true);
    assertTrue(jsonArray.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAsList() {
    jsonArray.add("a");
    jsonArray.add("b");
    assertEquals(2, jsonArray.asList().size());
    assertEquals("a", jsonArray.asList().get(0).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();
    array1.add("x");
    array2.add("x");
    assertEquals(array1, array2);
    assertEquals(array1.hashCode(), array2.hashCode());

    array2.add("y");
    assertNotEquals(array1, array2);
  }
}