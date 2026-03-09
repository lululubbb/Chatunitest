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

class ObjectTypeAdapter_552_3Test {

  @Test
    @Timeout(8000)
  void getFactory_withDoubleStrategy_returnsDoubleFactory() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
    // The returned factory should be the same instance as DOUBLE_FACTORY
    TypeAdapterFactory doubleFactory = getPrivateStaticField(ObjectTypeAdapter.class, "DOUBLE_FACTORY");
    assertSame(doubleFactory, factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_withCustomStrategy_returnsNewFactory() {
    ToNumberStrategy customStrategy = mock(ToNumberStrategy.class);
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(customStrategy);
    assertNotNull(factory);
    TypeAdapterFactory doubleFactory = getPrivateStaticField(ObjectTypeAdapter.class, "DOUBLE_FACTORY");
    assertNotSame(doubleFactory, factory);
  }

  @SuppressWarnings("unchecked")
  private static <T> T getPrivateStaticField(Class<?> clazz, String fieldName) {
    try {
      java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}