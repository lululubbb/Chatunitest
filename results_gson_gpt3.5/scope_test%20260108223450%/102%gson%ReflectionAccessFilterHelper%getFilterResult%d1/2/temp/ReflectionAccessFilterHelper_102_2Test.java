package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class ReflectionAccessFilterHelper_102_2Test {

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns first non-INDECISIVE FilterResult")
  void testGetFilterResultReturnsFirstNonIndecisive() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter3 = mock(ReflectionAccessFilter.class);

    Class<?> testClass = String.class;

    when(filter1.check(testClass)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(testClass)).thenReturn(FilterResult.valueOf("DENY"));
    when(filter3.check(testClass)).thenReturn(FilterResult.ALLOW);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2, filter3);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, testClass);

    assertEquals(FilterResult.valueOf("DENY"), result);

    verify(filter1).check(testClass);
    verify(filter2).check(testClass);
    verify(filter3, never()).check(testClass);
  }

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns ALLOW if all filters return INDECISIVE")
  void testGetFilterResultReturnsAllowIfAllIndecisive() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    Class<?> testClass = Integer.class;

    when(filter1.check(testClass)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(testClass)).thenReturn(FilterResult.INDECISIVE);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, testClass);

    assertEquals(FilterResult.ALLOW, result);

    verify(filter1).check(testClass);
    verify(filter2).check(testClass);
  }

  @Test
    @Timeout(8000)
  @DisplayName("getFilterResult returns ALLOW if filters list is empty")
  void testGetFilterResultReturnsAllowIfEmptyList() {
    List<ReflectionAccessFilter> filters = List.of();

    Class<?> testClass = Object.class;

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, testClass);

    assertEquals(FilterResult.ALLOW, result);
  }
}