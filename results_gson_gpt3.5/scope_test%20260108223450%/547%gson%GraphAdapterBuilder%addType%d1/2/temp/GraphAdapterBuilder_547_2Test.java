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
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GraphAdapterBuilder_547_2Test {

  private ConstructorConstructor constructorConstructorMock;
  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() {
    constructorConstructorMock = mock(ConstructorConstructor.class);
    graphAdapterBuilder = new GraphAdapterBuilder();

    // Inject mock ConstructorConstructor into GraphAdapterBuilder using reflection
    try {
      java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
      field.setAccessible(true);
      field.set(graphAdapterBuilder, constructorConstructorMock);
      java.lang.reflect.Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      instanceCreatorsField.setAccessible(true);
      // Use a mutable map instead of Collections.emptyMap()
      instanceCreatorsField.set(graphAdapterBuilder, new HashMap<>());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void addType_shouldReturnSameGraphAdapterBuilder_andUseObjectConstructor() {
    Type type = String.class;

    @SuppressWarnings({"rawtypes", "unchecked"})
    ObjectConstructor<?> objectConstructorMock = (ObjectConstructor<?>) mock(ObjectConstructor.class);
    Object constructedObject = new Object();

    // Fix: use any(TypeToken.class) in when() and thenAnswer to return objectConstructorMock
    when(constructorConstructorMock.get(any(TypeToken.class))).thenAnswer(invocation -> {
      TypeToken<?> arg = invocation.getArgument(0);
      if (arg.equals(TypeToken.get(type))) {
        return objectConstructorMock;
      }
      return null;
    });
    when(objectConstructorMock.construct()).thenReturn(constructedObject);

    GraphAdapterBuilder returnedBuilder = graphAdapterBuilder.addType(type);

    // The returned builder should be the same instance (this)
    assertSame(graphAdapterBuilder, returnedBuilder);

    // Verify that the instanceCreator creates the object returned by objectConstructor.construct()
    InstanceCreator<?> instanceCreator = getInstanceCreatorFromAddType(type);
    Object createdInstance = instanceCreator.createInstance(type);
    assertSame(constructedObject, createdInstance);
  }

  /**
   * Uses reflection to invoke the private addType(Type, InstanceCreator) method and get the
   * InstanceCreator argument passed.
   */
  private InstanceCreator<?> getInstanceCreatorFromAddType(Type type) {
    try {
      java.lang.reflect.Method addTypeMethod =
          GraphAdapterBuilder.class.getDeclaredMethod("addType", Type.class, InstanceCreator.class);
      addTypeMethod.setAccessible(true);

      final InstanceCreator<?>[] capturedInstanceCreator = new InstanceCreator<?>[1];
      // We create a spy GraphAdapterBuilder to capture the InstanceCreator parameter when addType(Type, InstanceCreator) is called
      GraphAdapterBuilder spyBuilder = Mockito.spy(graphAdapterBuilder);

      Mockito.doAnswer(invocation -> {
        capturedInstanceCreator[0] = invocation.getArgument(1);
        return spyBuilder;
      }).when(spyBuilder).addType(any(Type.class), any(InstanceCreator.class));

      // Invoke the public addType(Type) method on the spyBuilder so that our doAnswer is triggered
      spyBuilder.addType(type);

      return capturedInstanceCreator[0];
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}