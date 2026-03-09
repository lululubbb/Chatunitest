package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_424_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void size_emptyObject_returnsZero() {
    assertEquals(0, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void size_afterAddingOneElement_returnsOne() {
    jsonObject.addProperty("key1", "value1");
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void size_afterAddingMultipleElements_returnsCorrectSize() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.addProperty("key3", true);
    assertEquals(3, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void size_afterRemovingElement_decreasesSize() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", "value2");
    assertEquals(2, jsonObject.size());
    jsonObject.remove("key1");
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void size_afterRemovingNonExistingElement_sizeUnchanged() {
    jsonObject.addProperty("key1", "value1");
    assertEquals(1, jsonObject.size());
    jsonObject.remove("nonExistingKey");
    assertEquals(1, jsonObject.size());
  }
}