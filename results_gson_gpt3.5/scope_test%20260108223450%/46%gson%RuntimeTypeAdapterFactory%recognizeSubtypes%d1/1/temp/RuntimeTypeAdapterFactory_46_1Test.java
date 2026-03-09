package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RuntimeTypeAdapterFactory_46_1Test {

  @Test
    @Timeout(8000)
  public void testRecognizeSubtypes_returnsThisAndSetsFlag() throws Exception {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);

    // Initially, recognizeSubtypes should be false (private field)
    boolean initialRecognizeSubtypes = (boolean) getPrivateField(factory, "recognizeSubtypes");
    assertFalse(initialRecognizeSubtypes);

    // Call recognizeSubtypes method
    RuntimeTypeAdapterFactory<Object> returned = factory.recognizeSubtypes();

    // The method should return the same instance (this)
    assertSame(factory, returned);

    // The recognizeSubtypes flag should now be true
    boolean updatedRecognizeSubtypes = (boolean) getPrivateField(factory, "recognizeSubtypes");
    assertTrue(updatedRecognizeSubtypes);
  }

  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}