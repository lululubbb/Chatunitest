package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_102_5Test {

  @Test
    @Timeout(8000)
  void getFilterResult_returnsFirstNonIndecisiveResult() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter3 = mock(ReflectionAccessFilter.class);
    Class<?> clazz = String.class;

    when(filter1.check(clazz)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(clazz)).thenReturn(FilterResult.DISALLOW);
    // filter3 should never be called
    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2, filter3);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(FilterResult.DISALLOW, result);
    verify(filter1).check(clazz);
    verify(filter2).check(clazz);
    verify(filter3, never()).check(any());
  }

  @Test
    @Timeout(8000)
  void getFilterResult_allIndecisive_returnsAllow() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    Class<?> clazz = Integer.class;

    when(filter1.check(clazz)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(clazz)).thenReturn(FilterResult.INDECISIVE);
    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(FilterResult.ALLOW, result);
    verify(filter1).check(clazz);
    verify(filter2).check(clazz);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_emptyFilterList_returnsAllow() {
    Class<?> clazz = Double.class;
    List<ReflectionAccessFilter> filters = Collections.emptyList();

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, clazz);

    assertEquals(FilterResult.ALLOW, result);
  }
}