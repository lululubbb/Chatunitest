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

class ObjectTypeAdapter_552_2Test {

  @Test
    @Timeout(8000)
  void getFactory_withDoubleStrategy_returnsDoubleFactory() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
    assertSame(factory, ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE));
  }

  @Test
    @Timeout(8000)
  void getFactory_withCustomStrategy_returnsNewFactory() {
    ToNumberStrategy customStrategy = mock(ToNumberStrategy.class);
    TypeAdapterFactory factory1 = ObjectTypeAdapter.getFactory(customStrategy);
    TypeAdapterFactory factory2 = ObjectTypeAdapter.getFactory(customStrategy);
    assertNotNull(factory1);
    assertNotNull(factory2);
    // The returned factory is not the DOUBLE_FACTORY singleton
    assertNotSame(factory1, ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE));
    // Calling twice with same strategy returns different instances since newFactory creates new each time
    assertNotSame(factory1, factory2);
  }
}