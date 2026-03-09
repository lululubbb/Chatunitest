package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class NonNullElementWrapperList_475_5Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = Mockito.spy(new ArrayList<>());
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testSetReplacesElementAndReturnsOld() {
    delegate.add("old");
    String oldElement = list.set(0, "new");
    assertEquals("old", oldElement);
    assertEquals("new", delegate.get(0));
  }

  @Test
    @Timeout(8000)
  void testSetThrowsNullPointerExceptionForNullElement() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    // Confirm nonNull throws NPE on null
    InvocationTargetException ite = assertThrows(InvocationTargetException.class, () -> {
      nonNullMethod.invoke(list, (Object) null);
    });
    assertTrue(ite.getCause() instanceof NullPointerException);

    // Confirm set throws NPE on null element
    delegate.add("old");
    NullPointerException npe = assertThrows(NullPointerException.class, () -> list.set(0, null));
    assertNotNull(npe);
  }

  @Test
    @Timeout(8000)
  void testSetWithMultipleElements() {
    delegate.add("a");
    delegate.add("b");
    delegate.add("c");

    String replaced = list.set(1, "z");
    assertEquals("b", replaced);
    assertEquals("z", delegate.get(1));
  }

  @Test
    @Timeout(8000)
  void testSetWithIndexOutOfBounds() {
    delegate.add("a");
    assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "x"));
    assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "x"));
  }
}