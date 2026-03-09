package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectTypeAdapter_552_4Test {

  @Test
    @Timeout(8000)
  public void getFactory_withDoubleStrategy_returnsDoubleFactory() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
    // The returned factory should be the same as DOUBLE_FACTORY field
    TypeAdapterFactory doubleFactory = getDoubleFactoryField();
    assertSame(doubleFactory, factory);
  }

  @Test
    @Timeout(8000)
  public void getFactory_withCustomStrategy_returnsNewFactory() {
    ToNumberStrategy customStrategy = mock(ToNumberStrategy.class);
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(customStrategy);
    assertNotNull(factory);
    // It should not be the same instance as DOUBLE_FACTORY
    TypeAdapterFactory doubleFactory = getDoubleFactoryField();
    assertNotSame(doubleFactory, factory);
  }

  private TypeAdapterFactory getDoubleFactoryField() {
    try {
      java.lang.reflect.Field field = ObjectTypeAdapter.class.getDeclaredField("DOUBLE_FACTORY");
      field.setAccessible(true);
      return (TypeAdapterFactory) field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Failed to access DOUBLE_FACTORY field: " + e.getMessage());
      return null;
    }
  }
}