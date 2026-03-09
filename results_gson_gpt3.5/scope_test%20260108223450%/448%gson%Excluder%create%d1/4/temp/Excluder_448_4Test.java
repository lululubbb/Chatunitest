package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldAttributes;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Excluder_create_Test {

  private Excluder excluder;
  private Gson gson;
  private TypeToken<Object> typeToken;
  private Class<?> rawType;

  @BeforeEach
  void setUp() {
    excluder = spy(Excluder.DEFAULT);
    gson = mock(Gson.class);
    rawType = Object.class;
    typeToken = TypeToken.get(rawType);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNull_whenNoExclusion() {
    // Arrange
    doReturn(false).when(excluder).excludeClassChecks(rawType);
    doReturn(false).when(excluder).excludeClassInStrategy(rawType, true);
    doReturn(false).when(excluder).excludeClassInStrategy(rawType, false);

    // Act
    TypeAdapter<Object> adapter = excluder.create(gson, typeToken);

    // Assert
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapter_whenExcludeSerialize() throws IOException {
    // Arrange
    doReturn(true).when(excluder).excludeClassChecks(rawType);
    doReturn(false).when(excluder).excludeClassInStrategy(rawType, true);
    doReturn(false).when(excluder).excludeClassInStrategy(rawType, false);

    TypeAdapter<Object> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(eq(excluder), eq(typeToken))).thenReturn(delegateAdapter);

    TypeAdapter<Object> adapter = excluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object value = new Object();

    // Act & Assert
    adapter.write(jsonWriter, value);
    verify(jsonWriter).nullValue();

    JsonReader jsonReader = mock(JsonReader.class);
    assertNull(adapter.read(jsonReader));
    verify(jsonReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapter_whenExcludeDeserialize() throws IOException {
    // Arrange
    doReturn(false).when(excluder).excludeClassChecks(rawType);
    doReturn(false).when(excluder).excludeClassInStrategy(rawType, true);
    doReturn(true).when(excluder).excludeClassInStrategy(rawType, false);

    TypeAdapter<Object> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(eq(excluder), eq(typeToken))).thenReturn(delegateAdapter);

    TypeAdapter<Object> adapter = excluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object value = new Object();

    // Act & Assert
    adapter.write(jsonWriter, value);
    verify(delegateAdapter).write(jsonWriter, value);

    JsonReader jsonReader = mock(JsonReader.class);
    adapter.read(jsonReader);
    verify(jsonReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnTypeAdapter_whenExcludeBothSerializeAndDeserialize() throws IOException {
    // Arrange
    doReturn(true).when(excluder).excludeClassChecks(rawType);
    doReturn(true).when(excluder).excludeClassInStrategy(rawType, true);
    doReturn(true).when(excluder).excludeClassInStrategy(rawType, false);

    TypeAdapter<Object> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(eq(excluder), eq(typeToken))).thenReturn(delegateAdapter);

    TypeAdapter<Object> adapter = excluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object value = new Object();

    // Act & Assert
    adapter.write(jsonWriter, value);
    verify(jsonWriter).nullValue();

    JsonReader jsonReader = mock(JsonReader.class);
    assertNull(adapter.read(jsonReader));
    verify(jsonReader).skipValue();
  }

  @Test
    @Timeout(8000)
  void delegate_shouldLazilyInitializeDelegateAdapter() throws IOException {
    // Arrange
    doReturn(true).when(excluder).excludeClassChecks(rawType);
    doReturn(true).when(excluder).excludeClassInStrategy(rawType, true);
    doReturn(true).when(excluder).excludeClassInStrategy(rawType, false);

    TypeAdapter<Object> delegateAdapter = mock(TypeAdapter.class);
    when(gson.getDelegateAdapter(eq(excluder), eq(typeToken))).thenReturn(delegateAdapter);

    TypeAdapter<Object> adapter = excluder.create(gson, typeToken);
    assertNotNull(adapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    Object value = new Object();

    // Act
    adapter.write(jsonWriter, value);
    adapter.write(jsonWriter, value);

    // Assert delegate() called once and delegateAdapter's write called twice
    verify(gson, times(1)).getDelegateAdapter(eq(excluder), eq(typeToken));
    verify(delegateAdapter, times(0)).write(any(), any()); // first call should nullValue because skipSerialize true

    JsonReader jsonReader = mock(JsonReader.class);
    adapter.read(jsonReader);
    adapter.read(jsonReader);

    verify(gson, times(1)).getDelegateAdapter(eq(excluder), eq(typeToken));
    verify(jsonReader, times(2)).skipValue();
  }
}