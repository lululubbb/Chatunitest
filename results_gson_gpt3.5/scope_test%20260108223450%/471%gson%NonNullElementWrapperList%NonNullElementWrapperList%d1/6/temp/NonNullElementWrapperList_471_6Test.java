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

class NonNullElementWrapperList_471_6Test {

  private ArrayList<String> delegate;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void constructor_nullDelegate_throwsNPE() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> {
      new NonNullElementWrapperList<String>(null);
    });
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
  void set_nonNullElement_success() {
    delegate.add("x");
    String old = list.set(0, "y");
    assertEquals("x", old);
    assertEquals("y", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void set_nullElement_throwsNPE() {
    delegate.add("x");
    assertThrows(NullPointerException.class, () -> list.set(0, null));
  }

  @Test
    @Timeout(8000)
  void add_nonNullElement_success() {
    list.add(0, "z");
    assertEquals(1, delegate.size());
    assertEquals("z", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void add_nullElement_throwsNPE() {
    assertThrows(NullPointerException.class, () -> list.add(0, null));
  }

  @Test
    @Timeout(8000)
  void remove_index() {
    delegate.add("a");
    delegate.add("b");
    String removed = list.remove(0);
    assertEquals("a", removed);
    assertEquals(1, delegate.size());
    assertEquals("b", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void clear_delegates() {
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
    boolean removed = list.remove("a");
    assertTrue(removed);
    assertFalse(delegate.contains("a"));
  }

  @Test
    @Timeout(8000)
  void remove_object_notPresent() {
    delegate.add("a");
    boolean removed = list.remove("x");
    assertFalse(removed);
  }

  @Test
    @Timeout(8000)
  void removeAll_and_retainAll() {
    delegate.addAll(Arrays.asList("a", "b", "c"));
    Collection<String> toRemove = Arrays.asList("a", "c");
    boolean changed = list.removeAll(toRemove);
    assertTrue(changed);
    assertEquals(1, delegate.size());
    assertEquals("b", delegate.get(0));

    delegate.addAll(Arrays.asList("a", "c"));
    Collection<String> toRetain = Arrays.asList("a", "c");
    boolean changed2 = list.retainAll(toRetain);
    assertTrue(changed2);
    assertEquals(2, delegate.size());
    assertTrue(delegate.containsAll(toRetain));
  }

  @Test
    @Timeout(8000)
  void contains_indexOf_lastIndexOf() {
    delegate.addAll(Arrays.asList("a", "b", "a"));
    assertTrue(list.contains("a"));
    assertFalse(list.contains("x"));
    assertEquals(0, list.indexOf("a"));
    assertEquals(-1, list.indexOf("x"));
    assertEquals(2, list.lastIndexOf("a"));
    assertEquals(-1, list.lastIndexOf("x"));
  }

  @Test
    @Timeout(8000)
  void toArray_and_toArrayT() {
    delegate.addAll(Arrays.asList("a", "b"));
    Object[] arr = list.toArray();
    assertArrayEquals(new Object[] {"a", "b"}, arr);

    String[] arr2 = list.toArray(new String[0]);
    assertArrayEquals(new String[] {"a", "b"}, arr2);
  }

  @Test
    @Timeout(8000)
  void equals_and_hashCode() {
    delegate.addAll(Arrays.asList("a", "b"));
    NonNullElementWrapperList<String> list2 = new NonNullElementWrapperList<>(new ArrayList<>(delegate));
    assertEquals(list, list2);
    assertEquals(list.hashCode(), list2.hashCode());

    NonNullElementWrapperList<String> list3 = new NonNullElementWrapperList<>(new ArrayList<>());
    assertNotEquals(list, list3);
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Should return element when non-null
    String result = (String) nonNullMethod.invoke(list, "test");
    assertEquals("test", result);

    // Should throw NullPointerException when null
    assertThrows(InvocationTargetException.class, () -> {
      nonNullMethod.invoke(list, new Object[] {null});
    }, "Expected InvocationTargetException wrapping NullPointerException");
  }

  @Test
    @Timeout(8000)
  void constructor_reflection_nullDelegate_throwsNPE() throws Exception {
    Constructor<NonNullElementWrapperList> ctor = NonNullElementWrapperList.class.getConstructor(ArrayList.class);
    assertThrows(InvocationTargetException.class, () -> {
      ctor.newInstance((ArrayList<?>) null);
    });
  }
}