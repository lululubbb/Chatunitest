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

public class NonNullElementWrapperList_488_3Test {

  private NonNullElementWrapperList<String> list;
  private ArrayList<String> delegate;

  @BeforeEach
  public void setUp() throws Exception {
    delegate = new ArrayList<>();
    list = new NonNullElementWrapperList<>(delegate);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyDelegate() throws Exception {
    // delegate is empty
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonEmptyDelegate() throws Exception {
    delegate.add("one");
    delegate.add("two");
    delegate.add("three");
    int expectedHashCode = delegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_reflectionModifyDelegate() throws Exception {
    // Use reflection to replace delegate with a new ArrayList with different contents
    ArrayList<String> newDelegate = new ArrayList<>();
    newDelegate.add("alpha");
    newDelegate.add("beta");

    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, newDelegate);

    int expectedHashCode = newDelegate.hashCode();
    int actualHashCode = list.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }
}