package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GraphAdapterBuilder_547_1Test {

  private ConstructorConstructor constructorConstructor;
  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() {
    constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Use reflection to set the private final constructorConstructor field
    try {
      java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
      field.setAccessible(true);
      field.set(graphAdapterBuilder, constructorConstructor);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void addType_shouldReturnSameBuilderInstance() {
    Type type = String.class;

    @SuppressWarnings("unchecked")
    ObjectConstructor<Object> objectConstructor = (ObjectConstructor<Object>) Mockito.mock(ObjectConstructor.class);
    when(constructorConstructor.get(TypeToken.get(type))).thenReturn(objectConstructor);
    when(objectConstructor.construct()).thenReturn("constructedInstance");

    GraphAdapterBuilder spyBuilder = Mockito.spy(graphAdapterBuilder);
    // Spy addType(Type, InstanceCreator) to verify it is called and returns spyBuilder
    Mockito.doReturn(spyBuilder).when(spyBuilder).addType(any(Type.class), any(InstanceCreator.class));

    GraphAdapterBuilder result = spyBuilder.addType(type);

    // Verify returned instance is the same spyBuilder instance
    assertSame(spyBuilder, result);

    // Verify addType(Type, InstanceCreator) was called with the correct arguments
    Mockito.verify(spyBuilder).addType(Mockito.eq(type), Mockito.any(InstanceCreator.class));
  }
}