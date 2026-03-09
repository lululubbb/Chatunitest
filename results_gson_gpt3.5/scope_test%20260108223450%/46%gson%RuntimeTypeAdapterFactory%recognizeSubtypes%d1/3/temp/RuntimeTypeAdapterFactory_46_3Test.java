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

class RuntimeTypeAdapterFactory_46_3Test {

  @Test
    @Timeout(8000)
  void recognizeSubtypes_shouldSetFlagAndReturnSameInstance() throws Exception {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);

    // Initially reflectively check recognizeSubtypes field is false
    var recognizeSubtypesField = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    recognizeSubtypesField.setAccessible(true);
    boolean initialValue = recognizeSubtypesField.getBoolean(factory);
    assertFalse(initialValue);

    // Call method under test
    RuntimeTypeAdapterFactory<Object> returned = factory.recognizeSubtypes();

    // Check the returned instance is the same
    assertSame(factory, returned);

    // Check the recognizeSubtypes field is now true
    boolean updatedValue = recognizeSubtypesField.getBoolean(factory);
    assertTrue(updatedValue);
  }
}