package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

class NonNullElementWrapperList_472_2Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  void setUp() {
    delegate = new ArrayList<>();
    delegate.add("first");
    delegate.add("second");
    delegate.add("third");
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  void testGet_ValidIndex() {
    assertEquals("first", list.get(0));
    assertEquals("second", list.get(1));
    assertEquals("third", list.get(2));
  }

  @Test
    @Timeout(8000)
  void testGet_IndexOutOfBounds() {
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
  }
}