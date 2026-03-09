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
import java.lang.reflect.Method;

public class RuntimeTypeAdapterFactory_44_3Test {

  @Test
    @Timeout(8000)
  public void testOf_withBaseTypeAndTypeFieldName() throws Exception {
    // Use reflection to get the static method 'of' with two parameters
    Method ofMethod = RuntimeTypeAdapterFactory.class.getDeclaredMethod("of", Class.class, String.class);
    Object result = ofMethod.invoke(null, Object.class, "typeField");

    assertNotNull(result);
    assertTrue(result instanceof RuntimeTypeAdapterFactory);

    RuntimeTypeAdapterFactory<?> factory = (RuntimeTypeAdapterFactory<?>) result;

    // Check private fields via reflection
    var baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    var baseTypeValue = baseTypeField.get(factory);
    assertEquals(Object.class, baseTypeValue);

    var typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    var typeFieldNameValue = typeFieldNameField.get(factory);
    assertEquals("typeField", typeFieldNameValue);

    var maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    var maintainTypeValue = maintainTypeField.get(factory);
    assertEquals(false, maintainTypeValue);
  }
}