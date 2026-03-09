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

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

class RuntimeTypeAdapterFactory_46_6Test {

  @Test
    @Timeout(8000)
  void testRecognizeSubtypesReturnsThisAndSetsFlag() throws Exception {
    // Create instance via factory method
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);

    // Initially, recognizeSubtypes field should be false (default)
    // Use reflection to check private boolean recognizeSubtypes
    var recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    recognizeSubtypesField.setAccessible(true);
    boolean initialValue = recognizeSubtypesField.getBoolean(factory);
    assertFalse(initialValue, "Initial recognizeSubtypes should be false");

    // Call recognizeSubtypes method
    RuntimeTypeAdapterFactory<Object> returned = factory.recognizeSubtypes();

    // Verify the returned object is the same instance (this)
    assertSame(factory, returned, "recognizeSubtypes() should return this");

    // Verify the recognizeSubtypes field is now true
    boolean updatedValue = recognizeSubtypesField.getBoolean(factory);
    assertTrue(updatedValue, "recognizeSubtypes field should be true after calling recognizeSubtypes()");
  }
}