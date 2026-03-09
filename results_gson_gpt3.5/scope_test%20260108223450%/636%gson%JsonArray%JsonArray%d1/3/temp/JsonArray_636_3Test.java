package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_636_3Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() {
    jsonArray = new JsonArray();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructorCreatesEmptyJsonArray() {
    assertNotNull(jsonArray);
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithCapacity() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray arrayWithCapacity = constructor.newInstance(10);
    assertNotNull(arrayWithCapacity);
    assertEquals(0, arrayWithCapacity.size());
    assertTrue(arrayWithCapacity.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testAddAndGetElements() {
    jsonArray.add(new JsonPrimitive("test"));
    jsonArray.add(new JsonPrimitive(123));
    assertEquals(2, jsonArray.size());
    assertEquals("test", jsonArray.get(0).getAsString());
    assertEquals(123, jsonArray.get(1).getAsInt());
  }

  @Test
    @Timeout(8000)
  void testAddBooleanCharacterNumberStringAndJsonElement() {
    jsonArray.add(true);
    jsonArray.add('c');
    jsonArray.add(123);
    jsonArray.add("string");
    JsonPrimitive jsonPrimitive = new JsonPrimitive("elem");
    jsonArray.add(jsonPrimitive);

    assertEquals(5, jsonArray.size());
    assertEquals(true, jsonArray.get(0).getAsBoolean());
    assertEquals('c', jsonArray.get(1).getAsCharacter());
    assertEquals(123, jsonArray.get(2).getAsInt());
    assertEquals("string", jsonArray.get(3).getAsString());
    assertSame(jsonPrimitive, jsonArray.get(4));
  }

  @Test
    @Timeout(8000)
  void testAddAllAndContains() {
    JsonArray other = new JsonArray();
    JsonPrimitive elem1 = new JsonPrimitive("a");
    JsonPrimitive elem2 = new JsonPrimitive("b");
    other.add(elem1);
    other.add(elem2);

    jsonArray.addAll(other);

    assertEquals(2, jsonArray.size());
    assertTrue(jsonArray.contains(elem1));
    assertTrue(jsonArray.contains(elem2));
  }

  @Test
    @Timeout(8000)
  void testSetAndRemoveByIndexAndRemoveByElement() {
    JsonPrimitive elem1 = new JsonPrimitive("a");
    JsonPrimitive elem2 = new JsonPrimitive("b");
    JsonPrimitive elem3 = new JsonPrimitive("c");
    jsonArray.add(elem1);
    jsonArray.add(elem2);

    JsonElement replaced = jsonArray.set(1, elem3);
    assertSame(elem2, replaced);
    assertEquals(elem3, jsonArray.get(1));

    boolean removedByElement = jsonArray.remove(elem1);
    assertTrue(removedByElement);
    assertEquals(1, jsonArray.size());

    JsonElement removedByIndex = jsonArray.remove(0);
    assertEquals(elem3, removedByIndex);
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testIterator() {
    JsonPrimitive elem1 = new JsonPrimitive("x");
    JsonPrimitive elem2 = new JsonPrimitive("y");
    jsonArray.add(elem1);
    jsonArray.add(elem2);

    Iterator<JsonElement> iterator = jsonArray.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(elem1, iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals(elem2, iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    JsonArray array1 = new JsonArray();
    JsonArray array2 = new JsonArray();

    JsonPrimitive elem = new JsonPrimitive("elem");
    array1.add(elem);
    array2.add(elem);

    assertEquals(array1, array2);
    assertEquals(array1.hashCode(), array2.hashCode());

    array2.add(new JsonPrimitive("extra"));
    assertNotEquals(array1, array2);
  }

  @Test
    @Timeout(8000)
  void testDeepCopyCreatesNewInstanceWithCopiedElements() {
    jsonArray.add(new JsonPrimitive("a"));
    jsonArray.add(new JsonPrimitive(1));

    JsonArray copy = jsonArray.deepCopy();

    assertNotSame(jsonArray, copy);
    assertEquals(jsonArray.size(), copy.size());
    for (int i = 0; i < jsonArray.size(); i++) {
      assertEquals(jsonArray.get(i), copy.get(i));
      // Only assertNotSame if the element is mutable and deepCopy returns a new instance
      // JsonPrimitive is immutable for strings and numbers, so this check is not valid for them
      // So skip assertNotSame for JsonPrimitive instances
      if (!(jsonArray.get(i) instanceof JsonPrimitive)) {
        assertNotSame(jsonArray.get(i), copy.get(i)); // deep copy implies distinct instances for mutable elements
      }
    }
  }

  @Test
    @Timeout(8000)
  void testGetAsSingleElementReflectively() throws Exception {
    JsonPrimitive elem = new JsonPrimitive("single");
    jsonArray.add(elem);

    var method = JsonArray.class.getDeclaredMethod("getAsSingleElement");
    method.setAccessible(true);
    JsonElement result = (JsonElement) method.invoke(jsonArray);
    assertSame(elem, result);

    jsonArray.add(new JsonPrimitive("another"));

    Exception exception = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(jsonArray);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
  }

  @Test
    @Timeout(8000)
  void testGetAsNumberDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive(42));
    Number number = jsonArray.getAsNumber();
    assertEquals(42, number.intValue());
  }

  @Test
    @Timeout(8000)
  void testGetAsStringDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive("hello"));
    String str = jsonArray.getAsString();
    assertEquals("hello", str);
  }

  @Test
    @Timeout(8000)
  void testGetAsDoubleDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive(3.14));
    double d = jsonArray.getAsDouble();
    assertEquals(3.14, d, 0.0001);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimalDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive("123.456"));
    assertEquals(new java.math.BigDecimal("123.456"), jsonArray.getAsBigDecimal());
  }

  @Test
    @Timeout(8000)
  void testGetAsBigIntegerDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive("123456"));
    assertEquals(new java.math.BigInteger("123456"), jsonArray.getAsBigInteger());
  }

  @Test
    @Timeout(8000)
  void testGetAsFloatDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive(1.23f));
    assertEquals(1.23f, jsonArray.getAsFloat(), 0.0001);
  }

  @Test
    @Timeout(8000)
  void testGetAsLongDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive(123L));
    assertEquals(123L, jsonArray.getAsLong());
  }

  @Test
    @Timeout(8000)
  void testGetAsIntDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive(456));
    assertEquals(456, jsonArray.getAsInt());
  }

  @Test
    @Timeout(8000)
  void testGetAsByteDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive((byte) 12));
    assertEquals(12, jsonArray.getAsByte());
  }

  @Test
    @Timeout(8000)
  void testGetAsCharacterDeprecatedDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive('z'));
    assertEquals('z', jsonArray.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  void testGetAsShortDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive((short) 7));
    assertEquals(7, jsonArray.getAsShort());
  }

  @Test
    @Timeout(8000)
  void testGetAsBooleanDelegatesToSingleElement() {
    jsonArray.add(new JsonPrimitive(true));
    assertTrue(jsonArray.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  void testAsListReturnsUnmodifiableList() {
    JsonPrimitive elem = new JsonPrimitive("listElem");
    jsonArray.add(elem);
    List<JsonElement> list = jsonArray.asList();
    assertEquals(1, list.size());
    assertEquals(elem, list.get(0));
    // Modified to test unmodifiability by calling remove, which should throw UnsupportedOperationException
    assertThrows(UnsupportedOperationException.class, () -> {
      list.remove(0);
    });
  }
}