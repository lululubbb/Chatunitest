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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphAdapterBuilder_546_2Test {

  private GraphAdapterBuilder builder;

  @BeforeEach
  void setUp() {
    builder = new GraphAdapterBuilder();
  }

  @Test
    @Timeout(8000)
  void testConstructor_initializesFields() throws Exception {
    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Object instanceCreators = instanceCreatorsField.get(builder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators instanceof Map);

    Field constructorConstructorField = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    Object constructorConstructor = constructorConstructorField.get(builder);
    assertNotNull(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void testAddType_withTypeOnly_addsToInstanceCreators() throws Exception {
    Type type = String.class;
    GraphAdapterBuilder returned = builder.addType(type);
    assertSame(builder, returned);

    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreators = (Map<?, ?>) instanceCreatorsField.get(builder);
    assertTrue(instanceCreators.containsKey(type));
    assertNotNull(instanceCreators.get(type));
  }

  @Test
    @Timeout(8000)
  void testAddType_withTypeAndInstanceCreator_addsToInstanceCreators() throws Exception {
    Type type = Integer.class;
    @SuppressWarnings("unchecked")
    InstanceCreator<Integer> instanceCreator = mock(InstanceCreator.class);
    GraphAdapterBuilder returned = builder.addType(type, instanceCreator);
    assertSame(builder, returned);

    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreators = (Map<?, ?>) instanceCreatorsField.get(builder);
    assertTrue(instanceCreators.containsKey(type));
    assertSame(instanceCreator, instanceCreators.get(type));
  }

  @Test
    @Timeout(8000)
  void testRegisterOn_callsGsonBuilderRegisterTypeAdapterFactory() {
    GsonBuilder gsonBuilder = mock(GsonBuilder.class);
    when(gsonBuilder.registerTypeAdapterFactory(any())).thenReturn(gsonBuilder);

    builder.registerOn(gsonBuilder);

    verify(gsonBuilder).registerTypeAdapterFactory(any());
  }
}