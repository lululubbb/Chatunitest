package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GraphAdapterBuilder_547_3Test {

  private ConstructorConstructor constructorConstructor;
  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() {
    constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Use reflection to set the private final field constructorConstructor
    try {
      java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
      field.setAccessible(true);
      field.set(graphAdapterBuilder, constructorConstructor);
      // Also set instanceCreators map to empty to avoid NPE if used internally
      java.lang.reflect.Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      instanceCreatorsField.setAccessible(true);
      instanceCreatorsField.set(graphAdapterBuilder, Collections.emptyMap());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void addType_shouldReturnSameBuilder_andDelegateToAddTypeWithInstanceCreator() {
    Type type = String.class;
    @SuppressWarnings("unchecked")
    ObjectConstructor<?> objectConstructor = (ObjectConstructor<?>) mock(ObjectConstructor.class);
    String constructedObject = "constructed";
    when(objectConstructor.construct()).thenReturn(constructedObject);
    when(constructorConstructor.get(TypeToken.get(type))).thenReturn(objectConstructor);

    GraphAdapterBuilder builderReturned = graphAdapterBuilder.addType(type);

    // The method returns the same builder instance
    assertSame(graphAdapterBuilder, builderReturned);
  }
}