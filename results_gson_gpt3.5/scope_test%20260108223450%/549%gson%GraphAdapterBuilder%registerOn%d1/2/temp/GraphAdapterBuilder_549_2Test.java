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
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GraphAdapterBuilder_549_2Test {

  private GraphAdapterBuilder graphAdapterBuilder;
  private GsonBuilder gsonBuilderMock;

  @BeforeEach
  void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();
    gsonBuilderMock = mock(GsonBuilder.class);
  }

  @Test
    @Timeout(8000)
  void registerOn_shouldRegisterFactoryAndTypeAdapters() {
    // Prepare instanceCreators map with dummy entries via reflection
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    Type dummyType1 = String.class;
    Type dummyType2 = Integer.class;
    InstanceCreator<String> instanceCreator1 = mock(InstanceCreator.class);
    InstanceCreator<Integer> instanceCreator2 = mock(InstanceCreator.class);
    instanceCreators.put(dummyType1, instanceCreator1);
    instanceCreators.put(dummyType2, instanceCreator2);

    // Inject instanceCreators map into graphAdapterBuilder via reflection
    setField(graphAdapterBuilder, "instanceCreators", instanceCreators);

    // Also inject ConstructorConstructor (not used in registerOn but required for constructor)
    setField(graphAdapterBuilder, "constructorConstructor", mock(com.google.gson.internal.ConstructorConstructor.class));

    // Call the focal method
    graphAdapterBuilder.registerOn(gsonBuilderMock);

    // Capture registered TypeAdapterFactory
    ArgumentCaptor<TypeAdapterFactory> factoryCaptor = ArgumentCaptor.forClass(TypeAdapterFactory.class);
    verify(gsonBuilderMock).registerTypeAdapterFactory(factoryCaptor.capture());
    TypeAdapterFactory factory = factoryCaptor.getValue();
    assertNotNull(factory);

    // Verify registerTypeAdapter called for each key with the same factory
    for (Type key : instanceCreators.keySet()) {
      verify(gsonBuilderMock).registerTypeAdapter(key, factory);
    }

    // Verify no more interactions
    verifyNoMoreInteractions(gsonBuilderMock);
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}