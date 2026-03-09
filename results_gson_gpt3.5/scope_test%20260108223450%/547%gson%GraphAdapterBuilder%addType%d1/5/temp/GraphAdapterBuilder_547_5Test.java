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

class GraphAdapterBuilder_547_5Test {

  private ConstructorConstructor constructorConstructorMock;
  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  void setUp() throws Exception {
    constructorConstructorMock = Mockito.mock(ConstructorConstructor.class);

    graphAdapterBuilder = new GraphAdapterBuilder();

    // Inject the mock ConstructorConstructor into the private final field constructorConstructor
    java.lang.reflect.Field field = GraphAdapterBuilder.class.getDeclaredField("constructorConstructor");
    field.setAccessible(true);
    field.set(graphAdapterBuilder, constructorConstructorMock);

    // Also inject empty instanceCreators map to avoid NPE if used
    java.lang.reflect.Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    instanceCreatorsField.set(graphAdapterBuilder, Collections.emptyMap());
  }

  @Test
    @Timeout(8000)
  void addType_shouldReturnSameBuilderAndUseObjectConstructor() {
    Type type = String.class;

    // Mock ObjectConstructor<?> to return a specific instance
    @SuppressWarnings("unchecked")
    ObjectConstructor<Object> objectConstructorMock = (ObjectConstructor<Object>) Mockito.mock(ObjectConstructor.class);
    Object constructedInstance = new Object();
    when(objectConstructorMock.construct()).thenReturn(constructedInstance);

    // Create a TypeToken with the exact type to match the method signature
    TypeToken<String> typeToken = TypeToken.get((Class<String>) type);

    // Mock ConstructorConstructor.get(TypeToken) to return the mocked ObjectConstructor
    when(constructorConstructorMock.get(typeToken)).thenReturn(objectConstructorMock);

    // Call addType
    GraphAdapterBuilder returnedBuilder = graphAdapterBuilder.addType(type);

    // Verify the returned builder is the same instance
    assertSame(graphAdapterBuilder, returnedBuilder);
  }
}