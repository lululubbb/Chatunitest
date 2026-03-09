package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_424_5Test {

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
  public void size_afterAddingOneMember_returnsOne() {
    jsonObject.addProperty("key1", "value1");
    assertEquals(1, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void size_afterAddingMultipleMembers_returnsCorrectSize() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.addProperty("key3", true);
    assertEquals(3, jsonObject.size());
  }

  @Test
    @Timeout(8000)
  public void size_afterRemovingMember_returnsCorrectSize() {
    jsonObject.addProperty("key1", "value1");
    jsonObject.addProperty("key2", 123);
    jsonObject.remove("key1");
    assertEquals(1, jsonObject.size());
  }
}