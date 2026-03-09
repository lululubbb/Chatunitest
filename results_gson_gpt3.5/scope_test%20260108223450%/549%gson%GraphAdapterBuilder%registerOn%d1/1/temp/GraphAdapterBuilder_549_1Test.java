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

class GraphAdapterBuilder_549_1Test {

  private GraphAdapterBuilder graphAdapterBuilder;
  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Use reflection to set the private final instanceCreators field with a test map
    try {
      var instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      instanceCreatorsField.setAccessible(true);
      Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
      // Add a dummy Type and InstanceCreator to the map to test the for loop branch
      instanceCreators.put(String.class, mock(InstanceCreator.class));
      instanceCreatorsField.set(graphAdapterBuilder, instanceCreators);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    gsonBuilder = mock(GsonBuilder.class);
  }

  @Test
    @Timeout(8000)
  void registerOn_registersFactoryAndTypeAdapters() {
    // Call the method under test
    graphAdapterBuilder.registerOn(gsonBuilder);

    // Capture the factory registered by registerTypeAdapterFactory
    ArgumentCaptor<TypeAdapterFactory> factoryCaptor = ArgumentCaptor.forClass(TypeAdapterFactory.class);
    verify(gsonBuilder).registerTypeAdapterFactory(factoryCaptor.capture());
    TypeAdapterFactory factory = factoryCaptor.getValue();
    // The factory should not be null
    assert factory != null;

    // Capture the calls to registerTypeAdapter for each instanceCreator entry
    ArgumentCaptor<Type> typeCaptor = ArgumentCaptor.forClass(Type.class);
    ArgumentCaptor<Object> adapterCaptor = ArgumentCaptor.forClass(Object.class);
    verify(gsonBuilder).registerTypeAdapter(typeCaptor.capture(), adapterCaptor.capture());

    // The type registered should be String.class as we put in the instanceCreators map
    assert typeCaptor.getValue().equals(String.class);
    // The adapter registered should be the same factory instance
    assert adapterCaptor.getValue() == factory;
  }
}