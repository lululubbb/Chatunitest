package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_473_2Test {

  private NonNullElementWrapperList<String> wrapperList;
  private ArrayList<String> delegate;

  @BeforeEach
  public void setUp() throws Exception {
    delegate = new ArrayList<>();
    wrapperList = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void testSize_emptyDelegate() {
    assertEquals(0, wrapperList.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_nonEmptyDelegate() throws Exception {
    delegate.add("one");
    delegate.add("two");
    delegate.add("three");
    assertEquals(3, wrapperList.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_reflectivelyModifyDelegate() throws Exception {
    // Using reflection to replace delegate with a new ArrayList with 2 elements
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    ArrayList<String> newDelegate = new ArrayList<>();
    newDelegate.add("alpha");
    newDelegate.add("beta");
    delegateField.set(wrapperList, newDelegate);

    assertEquals(2, wrapperList.size());
  }
}