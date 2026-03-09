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
import java.lang.reflect.Field;

public class RuntimeTypeAdapterFactory_44_1Test {

  @Test
    @Timeout(8000)
  public void testOf_withBaseTypeAndTypeFieldName() throws Exception {
    // Directly call the public static method of(Class<T>, String)
    RuntimeTypeAdapterFactory<Object> factory = RuntimeTypeAdapterFactory.of(Object.class, "typeField");
    assertNotNull(factory);

    // Verify baseType field via reflection
    Field baseTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("baseType");
    baseTypeField.setAccessible(true);
    Class<?> baseTypeValue = (Class<?>) baseTypeField.get(factory);
    assertEquals(Object.class, baseTypeValue);

    // Verify typeFieldName field via reflection
    Field typeFieldNameField = RuntimeTypeAdapterFactory.class.getDeclaredField("typeFieldName");
    typeFieldNameField.setAccessible(true);
    String typeFieldNameValue = (String) typeFieldNameField.get(factory);
    assertEquals("typeField", typeFieldNameValue);

    // Verify maintainType field via reflection
    Field maintainTypeField = RuntimeTypeAdapterFactory.class.getDeclaredField("maintainType");
    maintainTypeField.setAccessible(true);
    boolean maintainTypeValue = (boolean) maintainTypeField.get(factory);
    assertFalse(maintainTypeValue);
  }

}