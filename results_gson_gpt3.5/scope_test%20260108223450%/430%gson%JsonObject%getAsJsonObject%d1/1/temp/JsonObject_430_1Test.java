package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_430_1Test {

  private JsonObject jsonObject;
  private LinkedTreeMap<String, JsonElement> members;

  @BeforeEach
  public void setUp() throws Exception {
    jsonObject = new JsonObject();

    // Access private field 'members' via reflection
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    // Clear members map before each test to ensure isolation
    members.clear();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_existingMember_returnsJsonObject() {
    JsonObject expected = new JsonObject();
    members.put("testMember", expected);

    JsonObject actual = jsonObject.getAsJsonObject("testMember");

    assertSame(expected, actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_nonExistingMember_returnsNull() {
    JsonObject actual = jsonObject.getAsJsonObject("nonExisting");

    assertNull(actual);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_memberIsNotJsonObject_throwsClassCastException() {
    JsonElement mockElement = mock(JsonElement.class);
    members.put("notJsonObject", mockElement);

    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonObject("notJsonObject"));
  }
}