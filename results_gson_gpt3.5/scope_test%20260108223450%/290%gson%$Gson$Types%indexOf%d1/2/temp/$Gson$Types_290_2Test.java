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

public class $Gson$Types_290_2Test {

  private static Method indexOfMethod;

  @BeforeAll
  static void setup() throws NoSuchMethodException {
    indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementFound_returnsCorrectIndex() throws IllegalAccessException, InvocationTargetException {
    Object[] array = new Object[] { "a", "b", "c", "d" };
    Object toFind = "c";

    int index = (int) indexOfMethod.invoke(null, (Object) array, toFind);

    assertEquals(2, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementFirst_returnsZero() throws IllegalAccessException, InvocationTargetException {
    Object[] array = new Object[] { "first", "second", "third" };
    Object toFind = "first";

    int index = (int) indexOfMethod.invoke(null, (Object) array, toFind);

    assertEquals(0, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementLast_returnsLastIndex() throws IllegalAccessException, InvocationTargetException {
    Object[] array = new Object[] { "x", "y", "z" };
    Object toFind = "z";

    int index = (int) indexOfMethod.invoke(null, (Object) array, toFind);

    assertEquals(2, index);
  }

  @Test
    @Timeout(8000)
  void indexOf_elementNotFound_throwsNoSuchElementException() {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = "notFound";

    assertThrows(NoSuchElementException.class, () -> {
      try {
        indexOfMethod.invoke(null, (Object) array, toFind);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void indexOf_arrayWithNullElement_findsNull() {
    Object[] array = new Object[] { "a", null, "c" };
    Object toFind = null;

    assertThrows(InvocationTargetException.class, () -> {
      indexOfMethod.invoke(null, (Object) array, toFind);
    });
  }

  @Test
    @Timeout(8000)
  void indexOf_arrayWithNullElementToFindNotNull_throwsNoSuchElementException() {
    Object[] array = new Object[] { "a", null, "c" };
    Object toFind = "b";

    assertThrows(NoSuchElementException.class, () -> {
      try {
        indexOfMethod.invoke(null, (Object) array, toFind);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }
}