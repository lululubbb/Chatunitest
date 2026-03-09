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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GraphAdapterBuilder_547_4Test {

  private ConstructorConstructor constructorConstructor;
  private GraphAdapterBuilder builder;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    builder = new GraphAdapterBuilder();

    // Inject the mocked constructorConstructor into builder using reflection
    try {
      var field = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
      field.setAccessible(true);
      field.set(builder, constructorConstructor);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void addType_shouldReturnSameBuilderAndInvokeAddTypeWithInstanceCreator() throws Exception {
    Type type = String.class;
    @SuppressWarnings("unchecked")
    ObjectConstructor<Object> objectConstructor = mock(ObjectConstructor.class);
    Object constructedObject = new Object();
    when(objectConstructor.construct()).thenReturn(constructedObject);
    // Use a TypeToken with explicit type parameter to avoid capture issues
    TypeToken<Object> typeToken = (TypeToken<Object>) (TypeToken<?>) TypeToken.get(type);
    when(constructorConstructor.get(eq(typeToken))).thenReturn(objectConstructor);

    // Spy on builder to verify addType(Type, InstanceCreator) is called
    GraphAdapterBuilder spyBuilder = Mockito.spy(builder);

    // Do nothing when addType(Type, InstanceCreator) is called, just return spyBuilder
    Mockito.doReturn(spyBuilder).when(spyBuilder).addType(eq(type), any());

    GraphAdapterBuilder result = spyBuilder.addType(type);

    // Verify that addType(Type, InstanceCreator) was called with the correct type and an InstanceCreator that returns constructedObject
    Mockito.verify(spyBuilder).addType(eq(type), Mockito.argThat(ic -> {
      Object instance = ic.createInstance(type);
      return instance == constructedObject;
    }));

    // The returned builder should be the spyBuilder itself
    assertSame(spyBuilder, result);
  }
}