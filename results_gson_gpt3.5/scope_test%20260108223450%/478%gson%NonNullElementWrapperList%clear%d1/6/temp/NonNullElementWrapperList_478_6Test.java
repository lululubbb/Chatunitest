package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NonNullElementWrapperList_478_6Test {

  NonNullElementWrapperList<String> list;
  ArrayList<String> mockDelegate;

  @BeforeEach
  void setUp() throws Exception {
    mockDelegate = mock(ArrayList.class);
    list = new NonNullElementWrapperList<>(mockDelegate);

    // Inject mockDelegate into list via reflection to ensure delegate field is set properly
    Field delegateField = NonNullElementWrapperList.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(list, mockDelegate);
  }

  @Test
    @Timeout(8000)
  void clear_callsDelegateClear() {
    list.clear();
    verify(mockDelegate, times(1)).clear();
  }
}