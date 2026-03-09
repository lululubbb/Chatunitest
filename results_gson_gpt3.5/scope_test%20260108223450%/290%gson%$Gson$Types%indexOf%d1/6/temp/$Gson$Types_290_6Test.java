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
  public static void setUp() throws NoSuchMethodException {
    indexOfMethod = $Gson$Types.class.getDeclaredMethod("indexOf", Object[].class, Object.class);
    indexOfMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testIndexOf_foundAtBeginning() throws InvocationTargetException, IllegalAccessException {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = "a";
    int result = (int) indexOfMethod.invoke(null, new Object[] { array, toFind });
    assertEquals(0, result);
  }

  @Test
    @Timeout(8000)
  public void testIndexOf_foundAtMiddle() throws InvocationTargetException, IllegalAccessException {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = "b";
    int result = (int) indexOfMethod.invoke(null, new Object[] { array, toFind });
    assertEquals(1, result);
  }

  @Test
    @Timeout(8000)
  public void testIndexOf_foundAtEnd() throws InvocationTargetException, IllegalAccessException {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = "c";
    int result = (int) indexOfMethod.invoke(null, new Object[] { array, toFind });
    assertEquals(2, result);
  }

  @Test
    @Timeout(8000)
  public void testIndexOf_notFound_throws() {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = "d";
    assertThrows(NoSuchElementException.class, () -> {
      try {
        indexOfMethod.invoke(null, new Object[] { array, toFind });
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testIndexOf_nullToFind_throws() {
    Object[] array = new Object[] { "a", "b", "c" };
    Object toFind = null;
    assertThrows(NullPointerException.class, () -> {
      try {
        indexOfMethod.invoke(null, new Object[] { array, toFind });
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  public void testIndexOf_arrayWithNullElement() {
    Object[] array = new Object[] { "a", null, "c" };
    Object toFind = null;
    assertThrows(NullPointerException.class, () -> {
      try {
        indexOfMethod.invoke(null, new Object[] { array, toFind });
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }
}