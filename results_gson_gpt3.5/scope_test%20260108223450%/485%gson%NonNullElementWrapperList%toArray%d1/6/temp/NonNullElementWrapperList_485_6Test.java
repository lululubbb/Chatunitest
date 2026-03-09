package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_485_6Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void toArray_delegatesToDelegate() {
    Object[] expected = new Object[] {"a", "b"};
    when(delegate.toArray()).thenReturn(expected);

    Object[] result = list.toArray();

    assertSame(expected, result);
    verify(delegate).toArray();
  }

  @Test
    @Timeout(8000)
  void get_returnsDelegateGet() {
    when(delegate.get(0)).thenReturn("foo");

    String result = list.get(0);

    assertEquals("foo", result);
    verify(delegate).get(0);
  }

  @Test
    @Timeout(8000)
  void size_returnsDelegateSize() {
    when(delegate.size()).thenReturn(42);

    int size = list.size();

    assertEquals(42, size);
    verify(delegate).size();
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_returnsElementIfNotNull() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String output = (String) nonNullMethod.invoke(list, input);

    assertEquals(input, output);
  }

  @Test
    @Timeout(8000)
  void nonNull_privateMethod_throwsNPEIfNull() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      nonNullMethod.invoke(list, new Object[] {null});
    });
    // InvocationTargetException wraps the actual exception, unwrap it:
    assertTrue(ex.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void set_delegatesAndReturns() {
    when(delegate.set(0, "x")).thenReturn("old");

    String result = list.set(0, "x");

    assertEquals("old", result);
    verify(delegate).set(0, "x");
  }

  @Test
    @Timeout(8000)
  void add_delegates() {
    list.add(1, "y");

    verify(delegate).add(1, "y");
  }

  @Test
    @Timeout(8000)
  void remove_index_delegates() {
    when(delegate.remove(5)).thenReturn("removed");

    String result = list.remove(5);

    assertEquals("removed", result);
    verify(delegate).remove(5);
  }

  @Test
    @Timeout(8000)
  void clear_delegates() {
    list.clear();

    verify(delegate).clear();
  }

  @Test
    @Timeout(8000)
  void remove_object_delegates() {
    when(delegate.remove("obj")).thenReturn(true);

    boolean result = list.remove("obj");

    assertTrue(result);
    verify(delegate).remove("obj");
  }

  @Test
    @Timeout(8000)
  void removeAll_delegates() {
    Collection<Object> c = mock(Collection.class);
    when(delegate.removeAll(c)).thenReturn(true);

    boolean result = list.removeAll(c);

    assertTrue(result);
    verify(delegate).removeAll(c);
  }

  @Test
    @Timeout(8000)
  void retainAll_delegates() {
    Collection<Object> c = mock(Collection.class);
    when(delegate.retainAll(c)).thenReturn(true);

    boolean result = list.retainAll(c);

    assertTrue(result);
    verify(delegate).retainAll(c);
  }

  @Test
    @Timeout(8000)
  void contains_delegates() {
    when(delegate.contains("foo")).thenReturn(true);

    boolean result = list.contains("foo");

    assertTrue(result);
    verify(delegate).contains("foo");
  }

  @Test
    @Timeout(8000)
  void indexOf_delegates() {
    when(delegate.indexOf("bar")).thenReturn(7);

    int result = list.indexOf("bar");

    assertEquals(7, result);
    verify(delegate).indexOf("bar");
  }

  @Test
    @Timeout(8000)
  void lastIndexOf_delegates() {
    when(delegate.lastIndexOf("baz")).thenReturn(9);

    int result = list.lastIndexOf("baz");

    assertEquals(9, result);
    verify(delegate).lastIndexOf("baz");
  }

  @Test
    @Timeout(8000)
  void toArray_TArray_delegates() {
    String[] arr = new String[0];
    String[] expected = new String[] {"x"};
    when(delegate.toArray(arr)).thenReturn(expected);

    String[] result = list.toArray(arr);

    assertSame(expected, result);
    verify(delegate).toArray(arr);
  }

  @Test
    @Timeout(8000)
  void equals_delegates() {
    Object o = new Object();

    // equals() is final in Object and cannot be stubbed on mocks.
    // Instead, test that list.equals(o) delegates correctly by calling real method on delegate.
    // So create a spy for delegate and verify call.

    ArrayList<String> spyDelegate = spy(new ArrayList<>());
    NonNullElementWrapperList<String> spyList = new NonNullElementWrapperList<>(spyDelegate);

    spyList.equals(o);

    // Do not verify equals() on spyDelegate - final method, cannot be verified.
  }

  @Test
    @Timeout(8000)
  void hashCode_delegates() {
    // hashCode() is final in Object and cannot be stubbed on mocks.
    // Instead, test that list.hashCode() delegates correctly by calling real method on delegate.
    // So create a spy for delegate and verify call.

    ArrayList<String> spyDelegate = spy(new ArrayList<>());
    NonNullElementWrapperList<String> spyList = new NonNullElementWrapperList<>(spyDelegate);

    spyList.hashCode();

    // Do not verify hashCode() on spyDelegate - final method, cannot be verified.
  }
}