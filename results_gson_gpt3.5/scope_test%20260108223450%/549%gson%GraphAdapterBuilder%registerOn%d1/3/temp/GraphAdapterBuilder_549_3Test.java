package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
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
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.graph.GraphAdapterBuilder.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class GraphAdapterBuilder_549_3Test {

  private GraphAdapterBuilder graphAdapterBuilder;
  private GsonBuilder gsonBuilderMock;

  @BeforeEach
  void setUp() {
    gsonBuilderMock = mock(GsonBuilder.class);
  }

  @Test
    @Timeout(8000)
  void registerOn_shouldRegisterFactoryAndTypeAdapters() {
    // Prepare instanceCreators map with mock InstanceCreator
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    Type type1 = String.class;
    InstanceCreator<?> instanceCreator1 = mock(InstanceCreator.class);
    instanceCreators.put(type1, instanceCreator1);

    // Create GraphAdapterBuilder with instanceCreators set via reflection
    graphAdapterBuilder = new GraphAdapterBuilder();
    setInstanceCreators(graphAdapterBuilder, instanceCreators);

    // Call method under test
    graphAdapterBuilder.registerOn(gsonBuilderMock);

    // Verify Factory creation and registration
    verify(gsonBuilderMock).registerTypeAdapterFactory(any(TypeAdapterFactory.class));
    // Verify registration of type adapters for each entry
    verify(gsonBuilderMock).registerTypeAdapter(eq(type1), any(TypeAdapterFactory.class));
    verifyNoMoreInteractions(gsonBuilderMock);
  }

  private void setInstanceCreators(GraphAdapterBuilder builder, Map<Type, InstanceCreator<?>> instanceCreators) {
    try {
      var field = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
      field.setAccessible(true);
      field.set(builder, instanceCreators);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}