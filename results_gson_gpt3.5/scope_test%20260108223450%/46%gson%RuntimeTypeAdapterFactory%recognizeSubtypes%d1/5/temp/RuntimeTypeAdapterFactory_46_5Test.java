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

class RuntimeTypeAdapterFactory_46_5Test {

  @Test
    @Timeout(8000)
  void testRecognizeSubtypesReturnsThisAndSetsFlag() throws Exception {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);

    // Initially recognizeSubtypes should be false (default)
    boolean initialFlag = getRecognizeSubtypesField(factory);
    assertFalse(initialFlag);

    // Call recognizeSubtypes()
    RuntimeTypeAdapterFactory<Object> returned = factory.recognizeSubtypes();

    // The returned instance is the same
    assertSame(factory, returned);

    // The recognizeSubtypes flag is now true
    boolean afterFlag = getRecognizeSubtypesField(factory);
    assertTrue(afterFlag);
  }

  private boolean getRecognizeSubtypesField(RuntimeTypeAdapterFactory<?> factory) throws Exception {
    var field = RuntimeTypeAdapterFactory.class.getDeclaredField("recognizeSubtypes");
    field.setAccessible(true);
    return field.getBoolean(factory);
  }
}