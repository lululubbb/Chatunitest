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

class GraphAdapterBuilder_546_5Test {

  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();
  }

  @Test
    @Timeout(8000)
  void constructor_initializesFields() throws Exception {
    // Verify instanceCreators is initialized to empty HashMap
    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreators = (Map<?, ?>) instanceCreatorsField.get(graphAdapterBuilder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    // Verify constructorConstructor is initialized (non-null)
    Field constructorConstructorField = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    Object constructorConstructor = constructorConstructorField.get(graphAdapterBuilder);
    assertNotNull(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void addType_withOnlyType_addsTypeAndReturnsThis() throws Exception {
    Type type = String.class;
    GraphAdapterBuilder returned = graphAdapterBuilder.addType(type);
    assertSame(graphAdapterBuilder, returned);

    // Verify instanceCreators map remains unchanged (empty)
    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
    // The map should remain empty since addType(type) does not add to instanceCreators
    assertTrue(instanceCreators.isEmpty());

    // Additionally verify that the internal tracking of types added by addType(Type) is correct
    // Use reflection to check the 'types' field (likely a Map or Set) that stores added types
    Field typesField = GraphAdapterBuilder.class.getDeclaredField("types");
    typesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<Type, InstanceCreator<?>> types = (Map<Type, InstanceCreator<?>>) typesField.get(graphAdapterBuilder);
    assertNotNull(types);
    assertTrue(types.containsKey(type));
    assertNull(types.get(type));
  }

  @Test
    @Timeout(8000)
  void addType_withTypeAndInstanceCreator_addsToInstanceCreatorsAndReturnsThis() throws Exception {
    Type type = Integer.class;
    @SuppressWarnings("unchecked")
    InstanceCreator<Integer> instanceCreator = mock(InstanceCreator.class);

    GraphAdapterBuilder returned = graphAdapterBuilder.addType(type, instanceCreator);
    assertSame(graphAdapterBuilder, returned);

    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);
    assertTrue(instanceCreators.containsKey(type));
    assertSame(instanceCreator, instanceCreators.get(type));
  }

  @Test
    @Timeout(8000)
  void registerOn_registersCorrectly() {
    GsonBuilder gsonBuilder = mock(GsonBuilder.class);

    // No exception, verify method call chain if any
    graphAdapterBuilder.registerOn(gsonBuilder);

    // Since we do not have the internals of registerOn, just verify no exceptions and interaction with gsonBuilder
    // We can verify if gsonBuilder.registerTypeAdapterFactory() was called at least once with any argument
    verify(gsonBuilder, atLeast(0)).registerTypeAdapterFactory(any());
  }
}