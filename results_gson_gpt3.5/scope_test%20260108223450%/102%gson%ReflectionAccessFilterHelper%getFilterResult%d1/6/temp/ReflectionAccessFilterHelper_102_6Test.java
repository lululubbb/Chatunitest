package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_102_6Test {

  @Test
    @Timeout(8000)
  void getFilterResult_returnsFirstNonIndecisiveResult() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter3 = mock(ReflectionAccessFilter.class);

    Class<?> testClass = String.class;

    when(filter1.check(testClass)).thenReturn(FilterResult.INDECISIVE);

    // Use reflection to get DENY enum constant (correct enum name)
    FilterResult deny = Enum.valueOf(FilterResult.class, "DENY");
    when(filter2.check(testClass)).thenReturn(deny);
    when(filter3.check(testClass)).thenReturn(FilterResult.ALLOW);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2, filter3);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, testClass);

    assertEquals(deny, result);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_returnsAllowIfAllIndecisive() {
    ReflectionAccessFilter filter1 = mock(ReflectionAccessFilter.class);
    ReflectionAccessFilter filter2 = mock(ReflectionAccessFilter.class);

    Class<?> testClass = Integer.class;

    when(filter1.check(testClass)).thenReturn(FilterResult.INDECISIVE);
    when(filter2.check(testClass)).thenReturn(FilterResult.INDECISIVE);

    List<ReflectionAccessFilter> filters = Arrays.asList(filter1, filter2);

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, testClass);

    assertEquals(FilterResult.ALLOW, result);
  }

  @Test
    @Timeout(8000)
  void getFilterResult_returnsAllowForEmptyFilterList() {
    Class<?> testClass = Double.class;

    List<ReflectionAccessFilter> filters = Collections.emptyList();

    FilterResult result = ReflectionAccessFilterHelper.getFilterResult(filters, testClass);

    assertEquals(FilterResult.ALLOW, result);
  }
}