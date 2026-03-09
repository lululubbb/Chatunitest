package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GsonTypesTest {

  private static Method indexOfMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException {
    indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void indexOf_findsElementInArray() throws IllegalAccessException, InvocationTargetException {
    Object[] array = new Object[] { "a", "b", "c" };
    int index = (int) indexOfMethod.invoke(null, (Object) array, "b");
    assertEquals(1, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_findsFirstOccurrence() throws IllegalAccessException, InvocationTargetException {
    Object[] array = new Object[] { "a", "b", "b", "c" };
    int index = (int) indexOfMethod.invoke(null, (Object) array, "b");
    assertEquals(1, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementNotFound_throwsNoSuchElementException() {
    Object[] array = new Object[] { "a", "b", "c" };
    assertThrows(NoSuchElementException.class, () -> {
      try {
        indexOfMethod.invoke(null, (Object) array, "d");
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void indexOf_nullElementInArray() throws IllegalAccessException, InvocationTargetException {
    Object[] array = new Object[] { "a", null, "c" };
    int index = (int) indexOfMethod.invoke(null, (Object) array, null);
    assertEquals(1, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_nullToFind_throwsNoSuchElementException() {
    Object[] array = new Object[] { "a", "b", "c" };
    assertThrows(NullPointerException.class, () -> {
      try {
        indexOfMethod.invoke(null, (Object) array, null);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }
}