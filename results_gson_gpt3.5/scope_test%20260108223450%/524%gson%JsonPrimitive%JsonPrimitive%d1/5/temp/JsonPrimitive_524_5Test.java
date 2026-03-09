package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonPrimitive_524_5Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithNumber_nonNull() throws Exception {
    Number number = 123;
    JsonPrimitive jp = new JsonPrimitive(number);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jp);
    assertSame(number, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithNumber_null_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Number) null));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_trueAndFalse() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    constructor.setAccessible(true);
    JsonPrimitive integralPrimitive = constructor.newInstance(123);
    JsonPrimitive nonIntegralPrimitive = constructor.newInstance(123.456);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean nonIntegralResult = (boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);

    assertTrue(integralResult);
    assertFalse(nonIntegralResult);
  }
}