package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectTypeAdapter_556_5Test {
  private Gson gsonMock;
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;
  private ObjectTypeAdapter objectTypeAdapter;

  @BeforeEach
  public void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Use reflection to invoke private constructor
    Constructor<ObjectTypeAdapter> constructor =
        ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, com.google.gson.ToNumberStrategy.class);
    constructor.setAccessible(true);
    objectTypeAdapter = constructor.newInstance(gsonMock, null);
  }

  @Test
    @Timeout(8000)
  public void testWrite_NullValue_CallsNullValue() throws IOException {
    objectTypeAdapter.write(jsonWriter, null);
    String json = stringWriter.toString();
    // JsonWriter.nullValue() writes "null"
    assert json.contains("null");
  }

  @Test
    @Timeout(8000)
  public void testWrite_TypeAdapterIsObjectTypeAdapter_WritesEmptyObject() throws IOException {
    Object value = new Object();

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> objectTypeAdapterCast = (TypeAdapter<Object>) (TypeAdapter<?>) objectTypeAdapter;

    // Use TypeToken to match generic type and avoid type mismatch
    when(gsonMock.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(objectTypeAdapterCast);

    objectTypeAdapter.write(jsonWriter, value);

    String json = stringWriter.toString();
    // Should write empty JSON object {}
    assert json.equals("{}");
  }

  @Test
    @Timeout(8000)
  public void testWrite_TypeAdapterIsOther_CallsWriteOnTypeAdapter() throws Exception {
    Object value = "testString";

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> typeAdapterMock = mock(TypeAdapter.class);

    // Use TypeToken to match generic type and avoid type mismatch
    when(gsonMock.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(typeAdapterMock);

    objectTypeAdapter.write(jsonWriter, value);

    // Verify that typeAdapter.write was called with jsonWriter and value
    verify(typeAdapterMock).write(jsonWriter, value);
  }
}