package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_493_2Test {

  @Mock
  private Gson mockGson;

  @Mock
  private TypeAdapterFactory mockSkipPast;

  @Mock
  private TypeAdapter<Object> mockDelegateAdapter;

  private TreeTypeAdapter<Object> treeTypeAdapter;

  private TypeToken<Object> typeToken = TypeToken.get(Object.class);

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    treeTypeAdapter = new TreeTypeAdapter<>(null, null, mockGson, typeToken, mockSkipPast, false);
  }

  @Test
    @Timeout(8000)
  public void delegate_returnsExistingDelegate_ifAlreadySet() throws Exception {
    // Set delegate field to mockDelegateAdapter
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, mockDelegateAdapter);

    // Access private method delegate()
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

    assertSame(mockDelegateAdapter, result);
    verifyNoInteractions(mockGson);
  }

  @Test
    @Timeout(8000)
  public void delegate_callsGsonGetDelegateAdapterAndCachesDelegate_ifDelegateIsNull() throws Exception {
    // Ensure delegate field is null
    Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
    delegateField.setAccessible(true);
    delegateField.set(treeTypeAdapter, null);

    when(mockGson.getDelegateAdapter(mockSkipPast, typeToken)).thenReturn(mockDelegateAdapter);

    // Access private method delegate()
    Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
    delegateMethod.setAccessible(true);

    TypeAdapter<Object> firstCall = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);
    TypeAdapter<Object> secondCall = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

    assertSame(mockDelegateAdapter, firstCall);
    assertSame(mockDelegateAdapter, secondCall);

    // Verify getDelegateAdapter called only once
    verify(mockGson, times(1)).getDelegateAdapter(mockSkipPast, typeToken);

    // Verify delegate field cached the adapter
    TypeAdapter<Object> cachedDelegate = (TypeAdapter<Object>) delegateField.get(treeTypeAdapter);
    assertSame(mockDelegateAdapter, cachedDelegate);
  }
}