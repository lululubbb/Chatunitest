package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
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
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_471_2Test {

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
    assertThrows(NullPointerException.class, () -> new NonNullElementWrapperList<>(null));
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
    delegate.add("a");
    String old = list.set(0, "b");
    assertEquals("a", old);
    assertEquals("b", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void set_nullElement_throwsNullPointerException() {
    delegate.add("a");
    assertThrows(NullPointerException.class, () -> list.set(0, null));
  }

  @Test
    @Timeout(8000)
  void add_insertsElement() {
    list.add(0, "a");
    assertEquals(1, delegate.size());
    assertEquals("a", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void add_nullElement_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> list.add(0, null));
  }

  @Test
    @Timeout(8000)
  void remove_byIndex() {
    delegate.add("a");
    delegate.add("b");
    String removed = list.remove(0);
    assertEquals("a", removed);
    assertEquals(1, delegate.size());
    assertEquals("b", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void clear_clearsDelegate() {
    delegate.add("a");
    delegate.add("b");
    list.clear();
    assertTrue(delegate.isEmpty());
  }

  @Test
    @Timeout(8000)
  void remove_object() {
    delegate.add("a");
    delegate.add("b");
    assertTrue(list.remove("a"));
    assertFalse(list.remove("c"));
    assertEquals(1, delegate.size());
    assertEquals("b", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void removeAll_collection() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");
    Collection<String> c = Arrays.asList("a", "c");
    assertTrue(list.removeAll(c));
    assertEquals(1, delegate.size());
    assertEquals("b", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void retainAll_collection() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");
    Collection<String> c = Arrays.asList("a", "c");
    assertTrue(list.retainAll(c));
    assertEquals(2, delegate.size());
    assertTrue(delegate.contains("a"));
    assertTrue(delegate.contains("c"));
    assertFalse(delegate.contains("b"));
  }

  @Test
    @Timeout(8000)
  void contains_object() {
    delegate.add("a");
    assertTrue(list.contains("a"));
    assertFalse(list.contains("b"));
  }

  @Test
    @Timeout(8000)
  void indexOf_object() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("a");
    assertEquals(0, list.indexOf("a"));
    assertEquals(1, list.indexOf("b"));
    assertEquals(-1, list.indexOf("c"));
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_object() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("a");
    assertEquals(2, list.lastIndexOf("a"));
    assertEquals(1, list.lastIndexOf("b"));
    assertEquals(-1, list.lastIndexOf("c"));
  }

  @Test
    @Timeout(8000)
  void toArray_noArg() {
    delegate.add("a");
    delegate.add("b");
    Object[] array = list.toArray();
    assertArrayEquals(new Object[] {"a", "b"}, array);
  }

  @Test
    @Timeout(8000)
  void toArray_withArg() {
    delegate.add("a");
    delegate.add("b");
    String[] arr = new String[2];
    String[] result = list.toArray(arr);
    assertSame(arr, result);
    assertArrayEquals(new String[] {"a", "b"}, arr);
  }

  @Test
    @Timeout(8000)
  void equals_and_hashCode() {
    delegate.add("a");
    delegate.add("b");
    ArrayList<String> otherDelegate = new ArrayList<>(delegate);
    NonNullElementWrapperList<String> otherList = new NonNullElementWrapperList<>(otherDelegate);
    assertEquals(list, otherList);
    assertEquals(list.hashCode(), otherList.hashCode());

    otherDelegate.add("c");
    assertNotEquals(list, otherList);
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_rejectsNull() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Should return the same object if not null
    String input = "test";
    Object result = nonNullMethod.invoke(list, input);
    assertSame(input, result);

    // Should throw NullPointerException if null
    assertThrows(InvocationTargetException.class, () -> nonNullMethod.invoke(list, new Object[] {null}));
  }
}