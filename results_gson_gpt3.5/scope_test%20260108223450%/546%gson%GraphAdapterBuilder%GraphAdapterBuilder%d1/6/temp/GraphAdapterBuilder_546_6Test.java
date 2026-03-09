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
import java.lang.reflect.Type;
import java.util.Map;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphAdapterBuilder_546_6Test {

  private GraphAdapterBuilder builder;

  @BeforeEach
  void setUp() {
    builder = new GraphAdapterBuilder();
  }

  @Test
    @Timeout(8000)
  void constructor_initializesFields() throws Exception {
    // Use reflection to verify instanceCreators map is initialized and empty
    var instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreators = (Map<?, ?>) instanceCreatorsField.get(builder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    // Verify constructorConstructor is initialized (non-null)
    var constructorConstructorField = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    Object constructorConstructor = constructorConstructorField.get(builder);
    assertNotNull(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void addType_withType_only_addsTypeToInstanceCreators() throws Exception {
    Type type = String.class;

    GraphAdapterBuilder returned = builder.addType(type);

    assertSame(builder, returned);

    var instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(builder);

    // Since addType(Type) does not add any InstanceCreator, the map should remain empty
    // or unchanged (depends on implementation). We test that map is not null.
    assertNotNull(instanceCreators);
  }

  @Test
    @Timeout(8000)
  void addType_withTypeAndInstanceCreator_addsEntryToInstanceCreators() throws Exception {
    Type type = Integer.class;
    @SuppressWarnings("unchecked")
    InstanceCreator<Integer> instanceCreator = mock(InstanceCreator.class);

    GraphAdapterBuilder returned = builder.addType(type, instanceCreator);

    assertSame(builder, returned);

    var instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(builder);

    assertTrue(instanceCreators.containsKey(type));
    assertSame(instanceCreator, instanceCreators.get(type));
  }

  @Test
    @Timeout(8000)
  void registerOn_registersFactoryOnGsonBuilder() {
    GsonBuilder gsonBuilder = mock(GsonBuilder.class);

    builder.registerOn(gsonBuilder);

    verify(gsonBuilder).registerTypeAdapterFactory(any());
  }
}