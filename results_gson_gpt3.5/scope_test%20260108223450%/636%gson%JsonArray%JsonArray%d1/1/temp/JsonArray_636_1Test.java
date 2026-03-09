package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.NonNullElementWrapperList;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonArray_636_1Test {

  private JsonArray jsonArray;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    jsonArray = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void testJsonArray_DefaultConstructor_InitializesEmptyList() {
    assertNotNull(jsonArray);
    assertEquals(0, jsonArray.size());
    assertTrue(jsonArray.isEmpty());
    assertFalse(jsonArray.iterator().hasNext());
  }

  @Test
    @Timeout(8000)
  void testJsonArray_ConstructorReflection_NewInstance() throws Exception {
    Constructor<JsonArray> constructor = JsonArray.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonArray array = constructor.newInstance();
    assertNotNull(array);
    assertEquals(0, array.size());
  }
}