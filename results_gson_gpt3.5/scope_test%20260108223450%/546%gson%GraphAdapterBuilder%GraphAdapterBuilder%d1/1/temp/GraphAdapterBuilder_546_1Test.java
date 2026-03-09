package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphAdapterBuilder_546_1Test {

  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();
  }

  @Test
    @Timeout(8000)
  void testConstructor_initializesFields() throws Exception {
    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators =
        (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    Field constructorConstructorField = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    Object constructorConstructor = constructorConstructorField.get(graphAdapterBuilder);
    assertNotNull(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void testAddType_withType_only_addsType() throws Exception {
    Type type = String.class;
    GraphAdapterBuilder returned = graphAdapterBuilder.addType(type);
    assertSame(graphAdapterBuilder, returned);

    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators =
        (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
    assertTrue(instanceCreators.containsKey(type));
    // The value is a dummy InstanceCreator, not null, so check it's not null instead of null
    assertNotNull(instanceCreators.get(type));
  }

  @Test
    @Timeout(8000)
  void testAddType_withTypeAndInstanceCreator_addsMapping() throws Exception {
    Type type = Integer.class;
    @SuppressWarnings("unchecked")
    InstanceCreator<Integer> instanceCreator = mock(InstanceCreator.class);
    GraphAdapterBuilder returned = graphAdapterBuilder.addType(type, instanceCreator);
    assertSame(graphAdapterBuilder, returned);

    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators =
        (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
    assertTrue(instanceCreators.containsKey(type));
    assertSame(instanceCreator, instanceCreators.get(type));
  }

  @Test
    @Timeout(8000)
  void testRegisterOn_registersFactoryOnGsonBuilder() {
    GsonBuilder gsonBuilder = mock(GsonBuilder.class);
    when(gsonBuilder.registerTypeAdapterFactory(any())).thenReturn(gsonBuilder);

    graphAdapterBuilder.registerOn(gsonBuilder);

    verify(gsonBuilder, times(1)).registerTypeAdapterFactory(any());
  }
}