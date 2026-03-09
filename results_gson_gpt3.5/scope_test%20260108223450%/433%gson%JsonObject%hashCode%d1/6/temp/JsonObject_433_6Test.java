package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_433_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyMembers() throws Exception {
    // members is empty initially
    int expectedHashCode = getMembersField(jsonObject).hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withMembers() throws Exception {
    JsonPrimitive value1 = new JsonPrimitive("value1");
    JsonPrimitive value2 = new JsonPrimitive(123);

    jsonObject.add("key1", value1);
    jsonObject.add("key2", value2);

    int expectedHashCode = getMembersField(jsonObject).hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_withNullMemberValue() throws Exception {
    // Add a JsonNull value explicitly by reflection to simulate null member value
    LinkedTreeMap<String, JsonElement> members = getMembersField(jsonObject);
    members.put("nullKey", JsonNull.INSTANCE);

    int expectedHashCode = members.hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @SuppressWarnings("unchecked")
  private LinkedTreeMap<String, JsonElement> getMembersField(JsonObject obj) throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    return (LinkedTreeMap<String, JsonElement>) membersField.get(obj);
  }
}