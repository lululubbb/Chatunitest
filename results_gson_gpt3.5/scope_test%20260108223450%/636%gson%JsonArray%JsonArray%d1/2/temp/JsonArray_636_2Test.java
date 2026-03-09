package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class JsonArray_636_2Test {

  @Test
    @Timeout(8000)
  public void testNoArgConstructor_initializesEmptyElements() throws Exception {
    JsonArray jsonArray = new JsonArray();
    assertNotNull(jsonArray);
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testCapacityConstructor_initializesElementsWithCapacity() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor(int.class);
    constructor.setAccessible(true);
    JsonArray jsonArray = constructor.newInstance(5);
    assertNotNull(jsonArray);
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
  }
}