package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NonNullElementWrapperList_486_6Test {

  private ArrayList<String> delegateMock;
  private NonNullElementWrapperList<String> list;

  @BeforeEach
  public void setUp() {
    delegateMock = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(delegateMock);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withEmptyArray_returnsDelegateToArray() {
    String[] input = new String[0];
    String[] expected = new String[0];
    when(delegateMock.toArray(input)).thenReturn(expected);

    String[] actual = list.toArray(input);

    verify(delegateMock).toArray(input);
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withNonEmptyArray_returnsDelegateToArray() {
    String[] input = new String[3];
    String[] expected = new String[] {"a", "b", "c"};
    when(delegateMock.toArray(input)).thenReturn(expected);

    String[] actual = list.toArray(input);

    verify(delegateMock).toArray(input);
    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testToArray_withNullArray_throwsNullPointerException() {
    // Since delegateMock is a mock, calling toArray(null) returns null instead of throwing.
    // To test the real behavior, create a real list and wrap it.
    ArrayList<String> realList = new ArrayList<>();
    NonNullElementWrapperList<String> realListWrapper = new NonNullElementWrapperList<>(realList);

    assertThrows(NullPointerException.class, () -> realListWrapper.<Object>toArray((Object[]) null));
  }

  @Test
    @Timeout(8000)
  public void testNonNullMethod_viaReflection() throws Exception {
    Method nonNullMethod = NonNullElementWrapperList.class.getDeclaredMethod("nonNull", Object.class);
    nonNullMethod.setAccessible(true);

    String input = "test";
    String result = (String) nonNullMethod.invoke(list, input);
    assertEquals("test", result);

    Exception exception = assertThrows(Exception.class, () -> nonNullMethod.invoke(list, (Object) null));
    assertTrue(exception.getCause() instanceof NullPointerException);
  }
}