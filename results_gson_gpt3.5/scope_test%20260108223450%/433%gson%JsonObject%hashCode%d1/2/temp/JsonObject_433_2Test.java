package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_433_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHashCode_emptyMembers() throws Exception {
    // When members is empty, hashCode should equal members.hashCode()
    int expectedHashCode = getMembersField(jsonObject).hashCode();
    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonEmptyMembers() throws Exception {
    // Add a property to members
    JsonPrimitive value = new JsonPrimitive("value");
    jsonObject.add("key", value);

    LinkedTreeMap<String, JsonElement> members = getMembersField(jsonObject);
    int expectedHashCode = members.hashCode();

    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_multipleEntries() throws Exception {
    jsonObject.addProperty("prop1", "stringValue");
    jsonObject.addProperty("prop2", 123);
    jsonObject.addProperty("prop3", true);
    jsonObject.addProperty("prop4", 'c');

    LinkedTreeMap<String, JsonElement> members = getMembersField(jsonObject);
    int expectedHashCode = members.hashCode();

    assertEquals(expectedHashCode, jsonObject.hashCode());
  }

  private LinkedTreeMap<String, JsonElement> getMembersField(JsonObject obj) throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    return (LinkedTreeMap<String, JsonElement>) membersField.get(obj);
  }
}