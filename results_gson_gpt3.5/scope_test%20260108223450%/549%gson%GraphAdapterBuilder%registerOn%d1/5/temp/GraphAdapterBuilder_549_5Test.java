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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapterFactory;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphAdapterBuilder_549_5Test {

  private GraphAdapterBuilder graphAdapterBuilder;
  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Use reflection to set private final field instanceCreators in GraphAdapterBuilder
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    instanceCreators.put(String.class, mock(InstanceCreator.class));
    instanceCreators.put(Integer.class, mock(InstanceCreator.class));
    try {
      java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      field.setAccessible(true);
      field.set(graphAdapterBuilder, instanceCreators);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }

    gsonBuilder = mock(GsonBuilder.class);
  }

  @Test
    @Timeout(8000)
  void registerOn_registersFactoryAndTypeAdapters() {
    graphAdapterBuilder.registerOn(gsonBuilder);

    // Verify that registerTypeAdapterFactory was called once with any TypeAdapterFactory
    verify(gsonBuilder).registerTypeAdapterFactory(org.mockito.ArgumentMatchers.any(TypeAdapterFactory.class));

    // Verify that registerTypeAdapter was called for each entry in instanceCreators
    verify(gsonBuilder).registerTypeAdapter(org.mockito.ArgumentMatchers.eq(String.class),
        org.mockito.ArgumentMatchers.any(TypeAdapterFactory.class));
    verify(gsonBuilder).registerTypeAdapter(org.mockito.ArgumentMatchers.eq(Integer.class),
        org.mockito.ArgumentMatchers.any(TypeAdapterFactory.class));
  }
}