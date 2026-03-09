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
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.mockito.Mockito.*;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapterFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class GraphAdapterBuilder_549_6Test {

  private GraphAdapterBuilder graphAdapterBuilder;
  private GsonBuilder mockGsonBuilder;

  @BeforeEach
  public void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Use reflection to set the private field instanceCreators in graphAdapterBuilder
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(String.class, mock(InstanceCreator.class));
    instanceCreators.put(Integer.class, mock(InstanceCreator.class));
    setInstanceCreators(graphAdapterBuilder, instanceCreators);

    mockGsonBuilder = mock(GsonBuilder.class);
    when(mockGsonBuilder.registerTypeAdapterFactory(any(TypeAdapterFactory.class))).thenReturn(mockGsonBuilder);
    when(mockGsonBuilder.registerTypeAdapter(any(Type.class), any())).thenReturn(mockGsonBuilder);
  }

  @Test
    @Timeout(8000)
  public void testRegisterOn_registersFactoryAndAdapters() {
    graphAdapterBuilder.registerOn(mockGsonBuilder);

    // Capture the factory registered
    ArgumentCaptor<TypeAdapterFactory> factoryCaptor = ArgumentCaptor.forClass(TypeAdapterFactory.class);
    verify(mockGsonBuilder).registerTypeAdapterFactory(factoryCaptor.capture());
    TypeAdapterFactory factory = factoryCaptor.getValue();
    assert factory != null;

    // Verify that registerTypeAdapter was called for each entry in instanceCreators
    verify(mockGsonBuilder, times(2)).registerTypeAdapter(any(Type.class), eq(factory));
    verify(mockGsonBuilder).registerTypeAdapter(String.class, factory);
    verify(mockGsonBuilder).registerTypeAdapter(Integer.class, factory);
  }

  private void setInstanceCreators(GraphAdapterBuilder builder, Map<Type, InstanceCreator<?>> instanceCreators) {
    try {
      java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      field.setAccessible(true);
      field.set(builder, instanceCreators);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}