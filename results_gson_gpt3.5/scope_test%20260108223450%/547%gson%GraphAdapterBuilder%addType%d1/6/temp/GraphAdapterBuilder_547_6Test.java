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

class GraphAdapterBuilder_547_6Test {

  private ConstructorConstructor constructorConstructor;
  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() {
    constructorConstructor = Mockito.mock(ConstructorConstructor.class);
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Use reflection to set private final constructorConstructor field
    try {
      var field = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
      field.setAccessible(true);
      field.set(graphAdapterBuilder, constructorConstructor);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void addType_shouldReturnSameGraphAdapterBuilderInstance() {
    Type type = String.class;

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> objectConstructor = (ObjectConstructor<?>) mock(ObjectConstructor.class);
    when(constructorConstructor.get(TypeToken.get(type))).thenReturn(objectConstructor);
    Object constructedInstance = new Object();
    when(objectConstructor.construct()).thenReturn(constructedInstance);

    GraphAdapterBuilder returned = graphAdapterBuilder.addType(type);

    // The returned instance should be the same instance (this)
    assertSame(graphAdapterBuilder, returned);
  }

  @Test
    @Timeout(8000)
  void addType_instanceCreatorCreatesConstructedObject() throws Exception {
    Type type = Integer.class;

    @SuppressWarnings("unchecked")
    ObjectConstructor<?> objectConstructor = (ObjectConstructor<?>) mock(ObjectConstructor.class);
    when(constructorConstructor.get(TypeToken.get(type))).thenReturn(objectConstructor);
    Object constructedInstance = Integer.valueOf(42);
    when(objectConstructor.construct()).thenReturn(constructedInstance);

    graphAdapterBuilder.addType(type);

    // Use reflection to get the private addType(Type, InstanceCreator) method and invoke it
    var addTypeMethod = GraphAdapterBuilder.class.getDeclaredMethod("addType", Type.class, InstanceCreator.class);
    addTypeMethod.setAccessible(true);

    InstanceCreator<Object> instanceCreator = new InstanceCreator<>() {
      @Override
      public Object createInstance(Type t) {
        return constructedInstance;
      }
    };

    Object result = addTypeMethod.invoke(graphAdapterBuilder, type, instanceCreator);
    assertSame(graphAdapterBuilder, result);
  }
}