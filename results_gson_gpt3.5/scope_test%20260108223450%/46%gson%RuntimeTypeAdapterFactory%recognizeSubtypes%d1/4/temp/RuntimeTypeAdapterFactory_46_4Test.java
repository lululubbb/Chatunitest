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

import java.lang.reflect.Field;

public class RuntimeTypeAdapterFactory_46_4Test {

  @Test
    @Timeout(8000)
  public void testRecognizeSubtypes_setsFlagAndReturnsThis() throws Exception {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);

    RuntimeTypeAdapterFactory<Object> returned = factory.recognizeSubtypes();

    assertSame(factory, returned, "recognizeSubtypes should return this instance");

    // Use reflection to verify private boolean recognizeSubtypes is set to true
    Field recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    recognizeSubtypesField.setAccessible(true);
    boolean flagValue = recognizeSubtypesField.getBoolean(factory);

    assertTrue(flagValue, "recognizeSubtypes field should be true after calling recognizeSubtypes()");
  }
}