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

class RuntimeTypeAdapterFactory_45_6Test {

  @Test
    @Timeout(8000)
  void testOf_withBaseType_createsInstance() {
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  void testOf_withNullBaseType_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      RuntimeTypeAdapterFactory.of(null);
    });
  }
}