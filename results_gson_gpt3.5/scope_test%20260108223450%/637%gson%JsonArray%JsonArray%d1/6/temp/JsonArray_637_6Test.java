package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonArray_637_6Test {

  private JsonArray jsonArray;

  @BeforeEach
  public void setUp() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    jsonArray = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithCapacity() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray arrayWithCapacity = constructor.newInstance(5);
    Field elementsField = JsonArray.class.getDeclaredField("elements");
    elementsField.setAccessible(true);
    Object elements = elementsField.get(arrayWithCapacity);
    assertNotNull(elements);
    assertTrue(elements instanceof java.util.ArrayList);
    assertEquals(0, ((java.util.ArrayList<?>) elements).size());
  }

  @Test
    @Timeout(8000)
  public void testAddAndSizeAndIsEmpty() {
    assertTrue(jsonArray.isEmpty());
    jsonArray.add(Boolean.TRUE);
    jsonArray.add(Character.valueOf('a'));
    jsonArray.add(123);
    jsonArray.add("test");
    JsonPrimitive primitive = new JsonPrimitive("elem");
    jsonArray.add(primitive);
    assertEquals(5, jsonArray.size());
    assertFalse(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddAllAndContains() {
    JsonArray other = new JsonArray();
    JsonPrimitive p1 = new JsonPrimitive("p1");
    JsonPrimitive p2 = new JsonPrimitive(10);
    other.add(p1);
    other.add(p2);

    jsonArray.addAll(other);
    assertEquals(2, jsonArray.size());
    assertTrue(jsonArray.contains(p1));
    assertTrue(jsonArray.contains(p2));
  }

  @Test
    @Timeout(8000)
  public void testSetAndGet() {
    JsonPrimitive p1 = new JsonPrimitive("p1");
    JsonPrimitive p2 = new JsonPrimitive(10);
    jsonArray.add(p1);
    JsonElement replaced = jsonArray.set(0, p2);
    assertEquals(p1, replaced);
    assertEquals(p2, jsonArray.get(0));
  }

  @Test
    @Timeout(8000)
  public void testRemoveByElementAndByIndex() {
    JsonPrimitive p1 = new JsonPrimitive("p1");
    JsonPrimitive p2 = new JsonPrimitive(10);
    jsonArray.add(p1);
    jsonArray.add(p2);

    boolean removed = jsonArray.remove(p1);
    assertTrue(removed);
    assertEquals(1, jsonArray.size());
    JsonElement removedElement = jsonArray.remove(0);
    assertEquals(p2, removedElement);
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIterator() {
    JsonPrimitive p1 = new JsonPrimitive("p1");
    JsonPrimitive p2 = new JsonPrimitive(10);
    jsonArray.add(p1);
    jsonArray.add(p2);

    Iterator<JsonElement> it = jsonArray.iterator();
    assertTrue(it.hasNext());
    assertEquals(p1, it.next());
    assertTrue(it.hasNext());
    assertEquals(p2, it.next());
    assertFalse(it.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testAsList() {
    JsonPrimitive p1 = new JsonPrimitive("p1");
    jsonArray.add(p1);
    List<JsonElement> list = jsonArray.asList();
    assertNotNull(list);
    assertEquals(1, list.size());
    assertEquals(p1, list.get(0));
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    JsonPrimitive p1 = new JsonPrimitive("p1");
    jsonArray.add(p1);

    JsonArray copy = jsonArray.deepCopy();
    assertNotSame(jsonArray, copy);
    assertEquals(jsonArray.size(), copy.size());
    assertEquals(jsonArray.get(0), copy.get(0));
  }

  @Test
    @Timeout(8000)
  public void testGetAsSingleElementPrivateMethod() throws Exception {
    // Add one element and invoke private getAsSingleElement
    JsonPrimitive p1 = new JsonPrimitive("p1");
    jsonArray.add(p1);

    Method method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    Object result = method.invoke(jsonArray);
    assertEquals(p1, result);

    // Add second element to cause exception
    JsonPrimitive p2 = new JsonPrimitive("p2");
    jsonArray.add(p2);
    Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber() {
    jsonArray.add(new JsonPrimitive(123));
    assertEquals(123, jsonArray.getAsNumber().intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString() {
    jsonArray.add(new JsonPrimitive("hello"));
    assertEquals("hello", jsonArray.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble() {
    jsonArray.add(new JsonPrimitive(12.34));
    assertEquals(12.34, jsonArray.getAsDouble());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal() {
    jsonArray.add(new JsonPrimitive(new BigDecimal("123.456")));
    assertEquals(new BigDecimal("123.456"), jsonArray.getAsBigDecimal());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger() {
    jsonArray.add(new JsonPrimitive(new BigInteger("123456")));
    assertEquals(new BigInteger("123456"), jsonArray.getAsBigInteger());
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat() {
    jsonArray.add(new JsonPrimitive(1.23f));
    assertEquals(1.23f, jsonArray.getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong() {
    jsonArray.add(new JsonPrimitive(123L));
    assertEquals(123L, jsonArray.getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt() {
    jsonArray.add(new JsonPrimitive(123));
    assertEquals(123, jsonArray.getAsInt());
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte() {
    jsonArray.add(new JsonPrimitive((byte) 12));
    assertEquals((byte) 12, jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter() {
    jsonArray.add(new JsonPrimitive('c'));
    assertEquals('c', jsonArray.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort() {
    jsonArray.add(new JsonPrimitive((short) 7));
    assertEquals((short) 7, jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean() {
    jsonArray.add(new JsonPrimitive(true));
    assertTrue(jsonArray.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonArray other = new JsonArray();
    JsonPrimitive p1 = new JsonPrimitive("p1");
    jsonArray.add(p1);
    other.add(p1);
    assertEquals(jsonArray, other);
    assertEquals(jsonArray.hashCode(), other.hashCode());

    other.add(new JsonPrimitive("extra"));
    assertNotEquals(jsonArray, other);
  }
}