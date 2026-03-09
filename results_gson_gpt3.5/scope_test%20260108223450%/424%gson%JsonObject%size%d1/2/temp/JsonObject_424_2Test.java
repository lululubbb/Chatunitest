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

public class JsonObject_424_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testSize_EmptyObject() {
    assertEquals(0, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_WithOneMember() {
    jsonObject.addProperty("key1", "value1");
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_WithMultipleMembers() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 10);
    jsonObject.addProperty("key3", true);
    assertEquals(3, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void testSize_AfterRemoveMember() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 10);
    jsonObject.remove("key1");
    assertEquals(1, jsonObject.size());
  }
}