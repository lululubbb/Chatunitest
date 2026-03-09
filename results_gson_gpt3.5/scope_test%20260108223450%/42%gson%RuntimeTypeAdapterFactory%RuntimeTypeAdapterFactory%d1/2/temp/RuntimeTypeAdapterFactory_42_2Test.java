package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapterFactory;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_42_2Test {

  private RuntimeTypeAdapterFactory<Base> factory;
  private Gson gson;

  static class Base {}
  static class SubA extends Base {}
  static class SubB extends Base {}

  @BeforeEach
  public void setUp() {
    factory = RuntimeTypeAdapterFactory.of(Base.class, "type", false)
        .registerSubtype(SubA.class, "a")
        .registerSubtype(SubB.class, "b");
    gson = mock(Gson.class);
  }

  @Test
    @Timeout(8000)
  public void testOfThrowsNullPointerException() {
    assertThrows(NullPointerException.class, () -> RuntimeTypeAdapterFactory.of(null, "type", false));
    assertThrows(NullPointerException.class, () -> RuntimeTypeAdapterFactory.of(Base.class, null, false));
  }

  @Test
    @Timeout(8000)
  public void testRecognizeSubtypes() throws Exception {
    RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type", false);
    assertFalse(getBooleanField(f, "recognizeSubtypes"));
    f.recognizeSubtypes();
    assertTrue(getBooleanField(f, "recognizeSubtypes"));
  }

  @Test
    @Timeout(8000)
  public void testRegisterSubtypeWithLabel() throws Exception {
    RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type", false);
    f.registerSubtype(SubA.class, "labelA");
    Map<String, Class<?>> labelToSubtype = getMapField(f, "labelToSubtype");
    Map<Class<?>, String> subtypeToLabel = getMapField(f, "subtypeToLabel");
    assertEquals(SubA.class, labelToSubtype.get("labelA"));
    assertEquals("labelA", subtypeToLabel.get(SubA.class));
  }

  @Test
    @Timeout(8000)
  public void testRegisterSubtypeWithoutLabel() throws Exception {
    RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type", false);
    f.registerSubtype(SubA.class);
    Map<String, Class<?>> labelToSubtype = getMapField(f, "labelToSubtype");
    Map<Class<?>, String> subtypeToLabel = getMapField(f, "subtypeToLabel");
    String label = subtypeToLabel.get(SubA.class);
    assertNotNull(label);
    assertEquals(SubA.class, labelToSubtype.get(label));
  }

  @Test
    @Timeout(8000)
  public void testCreateReturnsNullIfNotSubtype() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testCreateReturnsTypeAdapter() throws IOException {
    RuntimeTypeAdapterFactory<Base> f = RuntimeTypeAdapterFactory.of(Base.class, "type", false)
        .registerSubtype(SubA.class, "a")
        .registerSubtype(SubB.class, "b");

    TypeToken<Base> baseType = TypeToken.get(Base.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<Base> delegateAdapter = mock(TypeAdapter.class);

    Gson localGson = mock(Gson.class);
    when(localGson.getDelegateAdapter(f, baseType)).thenReturn(delegateAdapter);

    TypeAdapter<Base> adapter = f.create(localGson, baseType);
    assertNotNull(adapter);

    Base subA = new SubA();
    JsonWriter writer = mock(JsonWriter.class);
    JsonReader reader = mock(JsonReader.class);

    // Mock delegateAdapter write to do nothing
    doNothing().when(delegateAdapter).write(any(), any());

    // Test write method
    adapter.write(writer, subA);
    verify(delegateAdapter).write(writer, subA);

    // Mock delegateAdapter read to return SubA instance
    when(delegateAdapter.read(reader)).thenReturn(subA);

    Base readObj = adapter.read(reader);
    assertEquals(subA, readObj);
  }

  // Utility method to get private boolean field
  private boolean getBooleanField(Object obj, String fieldName) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.getBoolean(obj);
  }

  // Utility method to get private Map field
  @SuppressWarnings("unchecked")
  private <K, V> Map<K, V> getMapField(Object obj, String fieldName) throws Exception {
    Field field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (Map<K, V>) field.get(obj);
  }
}