package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
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

import static org.mockito.Mockito.*;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapterFactory;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;

class GraphAdapterBuilder_549_4Test {

  private GraphAdapterBuilder graphAdapterBuilder;
  private GsonBuilder gsonBuilderMock;

  @BeforeEach
  void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();
    gsonBuilderMock = mock(GsonBuilder.class);
  }

  @Test
    @Timeout(8000)
  void registerOn_registersFactoryAndInstanceCreators() {
    // Prepare a spy on the instanceCreators map inside GraphAdapterBuilder
    // We use reflection to set a controlled instanceCreators map with entries.
    Map<Type, InstanceCreator<?>> instanceCreators = Map.of(
        String.class, mock(InstanceCreator.class),
        Integer.class, mock(InstanceCreator.class)
    );
    setInstanceCreators(graphAdapterBuilder, instanceCreators);

    graphAdapterBuilder.registerOn(gsonBuilderMock);

    // Capture the factory registered
    ArgumentCaptor<TypeAdapterFactory> factoryCaptor = ArgumentCaptor.forClass(TypeAdapterFactory.class);
    verify(gsonBuilderMock).registerTypeAdapterFactory(factoryCaptor.capture());
    TypeAdapterFactory factory = factoryCaptor.getValue();
    assertNotNull(factory);

    // Verify registerTypeAdapter called for each entry with the factory
    for (Type key : instanceCreators.keySet()) {
      verify(gsonBuilderMock).registerTypeAdapter(eq(key), eq(factory));
    }
  }

  @Test
    @Timeout(8000)
  void registerOn_withEmptyInstanceCreators_onlyRegistersFactory() {
    // Set empty instanceCreators map
    setInstanceCreators(graphAdapterBuilder, Map.of());

    graphAdapterBuilder.registerOn(gsonBuilderMock);

    // Capture the factory registered
    ArgumentCaptor<TypeAdapterFactory> factoryCaptor = ArgumentCaptor.forClass(TypeAdapterFactory.class);
    verify(gsonBuilderMock).registerTypeAdapterFactory(factoryCaptor.capture());
    TypeAdapterFactory factory = factoryCaptor.getValue();
    assertNotNull(factory);

    // Verify registerTypeAdapter is never called because instanceCreators is empty
    verify(gsonBuilderMock, never()).registerTypeAdapter(any(), any());
  }

  private static void setInstanceCreators(GraphAdapterBuilder builder, Map<Type, InstanceCreator<?>> instanceCreators) {
    try {
      java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      field.setAccessible(true);
      field.set(builder, instanceCreators);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}