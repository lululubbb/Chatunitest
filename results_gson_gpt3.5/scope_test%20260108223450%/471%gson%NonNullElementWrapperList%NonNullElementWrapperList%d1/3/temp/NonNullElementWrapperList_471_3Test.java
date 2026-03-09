package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_471_3Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void constructor_nullDelegate_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> new NonNullElementWrapperList<>(null));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  void get_and_size() {
    delegate.add("a");
    delegate.add("b");
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
  }

  @Test
    @Timeout(8000)
  void set_replacesElement() {
    delegate.add("x");
    String old = list.set(0, "y");
    assertEquals("x", old);
    assertEquals("y", list.get(0));
  }

  @Test
    @Timeout(8000)
  void set_nullElement_throwsNullPointerException() {
    delegate.add("x");
    NullPointerException ex = assertThrows(NullPointerException.class, () -> list.set(0, null));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  void add_insertsElement() {
    list.add(0, "first");
    list.add(1, "second");
    assertEquals(2, list.size());
    assertEquals("first", list.get(0));
    assertEquals("second", list.get(1));
  }

  @Test
    @Timeout(8000)
  void add_nullElement_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> list.add(0, null));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  void remove_byIndex() {
    delegate.add("a");
    delegate.add("b");
    String removed = list.remove(0);
    assertEquals("a", removed);
    assertEquals(1, list.size());
    assertEquals("b", list.get(0));
  }

  @Test
    @Timeout(8000)
  void clear_clearsDelegate() {
    delegate.add("x");
    delegate.add("y");
    list.clear();
    assertTrue(list.isEmpty());
    assertEquals(0, delegate.size());
  }

  @Test
    @Timeout(8000)
  void remove_object() {
    delegate.add("a");
    delegate.add("b");
    assertTrue(list.remove("a"));
    assertFalse(list.remove("c"));
    assertEquals(1, list.size());
    assertEquals("b", list.get(0));
  }

  @Test
    @Timeout(8000)
  void removeAll_and_retainAll() {
    delegate.addAll(Arrays.asList("a", "b", "c", "d"));
    assertTrue(list.removeAll(Arrays.asList("b", "c")));
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("d", list.get(1));

    assertTrue(list.retainAll(Collections.singleton("a")));
    assertEquals(1, list.size());
    assertEquals("a", list.get(0));
  }

  @Test
    @Timeout(8000)
  void contains_indexOf_lastIndexOf() {
    delegate.addAll(Arrays.asList("a", "b", "a"));
    assertTrue(list.contains("a"));
    assertFalse(list.contains("z"));
    assertEquals(0, list.indexOf("a"));
    assertEquals(2, list.lastIndexOf("a"));
    assertEquals(-1, list.indexOf("z"));
    assertEquals(-1, list.lastIndexOf("z"));
  }

  @Test
    @Timeout(8000)
  void toArray_and_toArrayWithParameter() {
    delegate.addAll(Arrays.asList("a", "b"));
    Object[] arr = list.toArray();
    assertArrayEquals(new Object[] {"a", "b"}, arr);

    String[] arr2 = list.toArray(new String[0]);
    assertArrayEquals(new String[] {"a", "b"}, arr2);

    String[] arr3 = list.toArray(new String[5]);
    assertEquals("a", arr3[0]);
    assertEquals("b", arr3[1]);
    assertNull(arr3[2]);
  }

  @Test
    @Timeout(8000)
  void equals_and_hashCode() {
    delegate.addAll(Arrays.asList("a", "b"));
    NonNullElementWrapperList<String> other = new NonNullElementWrapperList<>(new ArrayList<>(Arrays.asList("a", "b")));
    NonNullElementWrapperList<String> different = new NonNullElementWrapperList<>(new ArrayList<>(Arrays.asList("a")));

    assertEquals(list, other);
    assertNotEquals(list, different);
    assertNotEquals(list, null);
    assertNotEquals(list, "string");

    assertEquals(list.hashCode(), other.hashCode());
    assertNotEquals(list.hashCode(), different.hashCode());
  }

  @Test
    @Timeout(8000)
  void testNonNull_usingReflection() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String output = (String) nonNullMethod.invoke(list, input);
    assertEquals(input, output);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      try {
        nonNullMethod.invoke(list, (Object) null);
      } catch (InvocationTargetException e) {
        throw e;
      }
    });
    assertTrue(ex.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void constructor_reflective() throws Exception {
    Constructor<NonNullElementWrapperList> constructor = NonNullElementWrapperList.class.getConstructor(ArrayList.class);
    NonNullElementWrapperList<String> instance = constructor.newInstance(new ArrayList<>());
    assertNotNull(instance);
  }
}