package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonPrimitive_534_3Test {

  private JsonPrimitive createJsonPrimitive(Object value) throws Exception {
    Constructor<JsonPrimitive> constructor = null;
    Class<?> valueClass = value.getClass();

    // Try to find a constructor with the exact parameter type or compatible one
    // JsonPrimitive constructors accept Boolean, Number, String, Character
    if (value instanceof Boolean) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
    } else if (value instanceof Number) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    } else if (value instanceof String) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(String.class);
    } else if (value instanceof Character) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(Character.class);
    } else {
      // fallback: try to find constructor with superclass or interfaces
      // specifically for LazilyParsedNumber (implements Number)
      Class<?> superClass = valueClass.getSuperclass();
      while (constructor == null && superClass != null) {
        try {
          constructor = JsonPrimitive.class.getDeclaredConstructor(superClass);
        } catch (NoSuchMethodException e) {
          superClass = superClass.getSuperclass();
        }
      }
      if (constructor == null) {
        // try interfaces
        for (Class<?> iface : valueClass.getInterfaces()) {
          try {
            constructor = JsonPrimitive.class.getDeclaredConstructor(iface);
            if (constructor != null) break;
          } catch (NoSuchMethodException ignored) {
          }
        }
      }
      if (constructor == null) {
        throw new NoSuchMethodException("No suitable JsonPrimitive constructor found for " + valueClass);
      }
    }

    constructor.setAccessible(true);
    return constructor.newInstance(value);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withNumber() throws Exception {
    // Use a Number type (Integer)
    JsonPrimitive primitive = createJsonPrimitive(42);

    double result = primitive.getAsDouble();

    assertEquals(42.0, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withStringNumber() throws Exception {
    // Use a String representing a number
    JsonPrimitive primitive = createJsonPrimitive("3.14159");

    double result = primitive.getAsDouble();

    assertEquals(3.14159, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withStringNonNumber() throws Exception {
    // Use a String representing a non-numeric string that can be parsed as double
    JsonPrimitive primitive = createJsonPrimitive("2.71828");

    double result = primitive.getAsDouble();

    assertEquals(2.71828, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withBooleanString_throwsNumberFormatException() throws Exception {
    JsonPrimitive primitive = createJsonPrimitive("true");

    assertThrows(NumberFormatException.class, primitive::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withCharacter() throws Exception {
    JsonPrimitive primitive = createJsonPrimitive('7');

    double result = primitive.getAsDouble();

    assertEquals(7.0, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is package private, so use reflection to create an instance
    Class<?> lazilyParsedNumberClass = Class.forName("com.google.gson.internal.LazilyParsedNumber");
    Constructor<?> lpnConstructor = lazilyParsedNumberClass.getDeclaredConstructor(String.class);
    lpnConstructor.setAccessible(true);
    Object lpnInstance = lpnConstructor.newInstance("123.456");

    JsonPrimitive primitive = createJsonPrimitive(lpnInstance);

    double result = primitive.getAsDouble();

    assertEquals(123.456, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_privateMethod() throws Exception {
    JsonPrimitive primitive = createJsonPrimitive(10);

    Method isNumberMethod = JsonPrimitive.class.getDeclaredMethod("isNumber");
    isNumberMethod.setAccessible(true);

    boolean result = (boolean) isNumberMethod.invoke(primitive);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withNegativeNumber() throws Exception {
    JsonPrimitive primitive = createJsonPrimitive(-99.99);

    double result = primitive.getAsDouble();

    assertEquals(-99.99, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_withStringScientificNotation() throws Exception {
    JsonPrimitive primitive = createJsonPrimitive("1.23e2");

    double result = primitive.getAsDouble();

    assertEquals(123.0, result, 0.000001);
  }
}