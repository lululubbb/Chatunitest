package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class JsonArray_636_4Test {

    @Test
    @Timeout(8000)
    public void testNoArgConstructor_initializesEmptyElements() throws Exception {
        JsonArray jsonArray = new JsonArray();
        assertNotNull(jsonArray);
        assertEquals(0, jsonArray.size());
        assertTrue(jsonArray.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithCapacity_initializesEmptyElements() throws Exception {
        Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        JsonArray jsonArray = constructor.newInstance(10);
        assertNotNull(jsonArray);
        assertEquals(0, jsonArray.size());
        assertTrue(jsonArray.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testAddAndGetElements() {
        JsonArray jsonArray = new JsonArray();

        JsonPrimitive boolElement = new JsonPrimitive(true);
        jsonArray.add(true);
        assertEquals(boolElement, jsonArray.get(0));

        JsonPrimitive charElement = new JsonPrimitive('a');
        jsonArray.add('a');
        assertEquals(charElement, jsonArray.get(1));

        JsonPrimitive numberElement = new JsonPrimitive(123);
        jsonArray.add(123);
        assertEquals(numberElement, jsonArray.get(2));

        JsonPrimitive stringElement = new JsonPrimitive("test");
        jsonArray.add("test");
        assertEquals(stringElement, jsonArray.get(3));

        JsonPrimitive element = new JsonPrimitive("element");
        jsonArray.add(element);
        assertEquals(element, jsonArray.get(4));
    }

    @Test
    @Timeout(8000)
    public void testAddAllAndContainsAndRemove() {
        JsonArray array1 = new JsonArray();
        JsonPrimitive element1 = new JsonPrimitive("one");
        JsonPrimitive element2 = new JsonPrimitive("two");
        array1.add(element1);

        JsonArray array2 = new JsonArray();
        array2.add(element2);

        array1.addAll(array2);
        assertEquals(2, array1.size());
        assertTrue(array1.contains(element1));
        assertTrue(array1.contains(element2));

        assertTrue(array1.remove(element1));
        assertFalse(array1.contains(element1));
        assertEquals(1, array1.size());

        JsonElement removed = array1.remove(0);
        assertEquals(element2, removed);
        assertTrue(array1.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testSetAndIterator() {
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive element1 = new JsonPrimitive("one");
        JsonPrimitive element2 = new JsonPrimitive("two");
        jsonArray.add(element1);

        JsonElement replaced = jsonArray.set(0, element2);
        assertEquals(element1, replaced);
        assertEquals(element2, jsonArray.get(0));

        Iterator<JsonElement> iterator = jsonArray.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testDeepCopyIndependence() {
        JsonArray original = new JsonArray();
        JsonPrimitive element = new JsonPrimitive("copy");
        original.add(element);

        JsonArray copy = original.deepCopy();
        assertNotSame(original, copy);
        assertEquals(original.size(), copy.size());
        assertEquals(original.get(0), copy.get(0));

        // Modify copy and verify original unchanged
        copy.set(0, new JsonPrimitive("changed"));
        assertEquals("copy", original.get(0).getAsString());
        assertEquals("changed", copy.get(0).getAsString());
    }

    @Test
    @Timeout(8000)
    public void testAsListEqualsHashCode() {
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive element = new JsonPrimitive("test");
        jsonArray.add(element);

        assertEquals(1, jsonArray.asList().size());
        assertTrue(jsonArray.equals(jsonArray));
        assertFalse(jsonArray.equals(null));
        assertFalse(jsonArray.equals("string"));

        JsonArray another = new JsonArray();
        another.add(element);
        assertEquals(jsonArray.hashCode(), another.hashCode());
        assertTrue(jsonArray.equals(another));
    }

    @Test
    @Timeout(8000)
    public void testGetAsSingleElementAndGetAsMethods() throws Exception {
        JsonArray jsonArray = new JsonArray();
        JsonPrimitive element = new JsonPrimitive(123);
        jsonArray.add(element);

        // Use reflection to access private getAsSingleElement()
        java.lang.reflect.Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);
        JsonElement single = (JsonElement) getAsSingleElement.invoke(jsonArray);
        assertEquals(element, single);

        assertEquals(element.getAsNumber(), jsonArray.getAsNumber());
        assertEquals(element.getAsString(), jsonArray.getAsString());
        assertEquals(element.getAsDouble(), jsonArray.getAsDouble());
        assertEquals(element.getAsBigDecimal(), jsonArray.getAsBigDecimal());
        assertEquals(element.getAsBigInteger(), jsonArray.getAsBigInteger());
        assertEquals(element.getAsFloat(), jsonArray.getAsFloat());
        assertEquals(element.getAsLong(), jsonArray.getAsLong());
        assertEquals(element.getAsInt(), jsonArray.getAsInt());
        assertEquals(element.getAsByte(), jsonArray.getAsByte());
        assertEquals(element.getAsCharacter(), jsonArray.getAsCharacter());
        assertEquals(element.getAsShort(), jsonArray.getAsShort());
        assertEquals(element.getAsBoolean(), jsonArray.getAsBoolean());
    }

    @Test
    @Timeout(8000)
    public void testGetAsSingleElement_throwsWhenEmpty() throws Exception {
        JsonArray jsonArray = new JsonArray();

        java.lang.reflect.Method getAsSingleElement = JsonArray.class.getDeclaredMethod("getAsSingleElement");
        getAsSingleElement.setAccessible(true);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                getAsSingleElement.invoke(jsonArray);
            } catch (InvocationTargetException e) {
                // unwrap the cause and throw it to be caught by assertThrows
                throw e.getCause();
            }
        });
        assertEquals("Array must have size 1, but has size 0", thrown.getMessage());
    }
}