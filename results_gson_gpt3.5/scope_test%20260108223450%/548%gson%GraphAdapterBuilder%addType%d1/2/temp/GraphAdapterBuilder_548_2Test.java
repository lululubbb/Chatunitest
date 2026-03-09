package com.google.gson.graph;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class GraphAdapterBuilder_548_2Test {

  private GraphAdapterBuilder graphAdapterBuilder;

  @BeforeEach
  public void setUp() {
    graphAdapterBuilder = new GraphAdapterBuilder();
  }

  @Test
    @Timeout(8000)
  public void addType_nullType_throwsNullPointerException() {
    InstanceCreator<?> instanceCreator = mock(InstanceCreator.class);
    assertThrows(NullPointerException.class, () -> graphAdapterBuilder.addType(null, instanceCreator));
  }

  @Test
    @Timeout(8000)
  public void addType_nullInstanceCreator_throwsNullPointerException() {
    Type type = String.class;
    assertThrows(NullPointerException.class, () -> graphAdapterBuilder.addType(type, null));
  }

  @Test
    @Timeout(8000)
  public void addType_validArguments_addsToInstanceCreatorsAndReturnsThis() throws Exception {
    Type type = String.class;
    @SuppressWarnings("unchecked")
    InstanceCreator<String> instanceCreator = mock(InstanceCreator.class);

    GraphAdapterBuilder returned = graphAdapterBuilder.addType(type, instanceCreator);
    assertSame(graphAdapterBuilder, returned);

    // Use reflection to access private field instanceCreators
    Field instanceCreatorsField = GraphAdapterBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(graphAdapterBuilder);

    assertTrue(instanceCreators.containsKey(type));
    assertSame(instanceCreator, instanceCreators.get(type));
  }
}