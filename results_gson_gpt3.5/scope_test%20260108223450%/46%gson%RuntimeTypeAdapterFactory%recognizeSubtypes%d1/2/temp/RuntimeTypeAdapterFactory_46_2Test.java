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

class RuntimeTypeAdapterFactory_46_2Test {

  @Test
    @Timeout(8000)
  void testRecognizeSubtypes_returnsThisAndSetsFlag() throws Exception {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);

    // Initially, recognizeSubtypes field should be false
    boolean initialFlag = (boolean) getPrivateField(factory, "recognizeSubtypes");
    assertFalse(initialFlag);

    // Call recognizeSubtypes method
    RuntimeTypeAdapterFactory<Object> returned = factory.recognizeSubtypes();

    // The method returns this
    assertSame(factory, returned);

    // The private boolean recognizeSubtypes should be true after call
    boolean flagAfterCall = (boolean) getPrivateField(factory, "recognizeSubtypes");
    assertTrue(flagAfterCall);
  }

  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    java.lang.reflect.Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}