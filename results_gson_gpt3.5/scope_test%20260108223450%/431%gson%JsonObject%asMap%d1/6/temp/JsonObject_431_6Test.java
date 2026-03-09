package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_431_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testAsMap_EmptyMembers() throws Exception {
    // Use reflection to clear the members map to ensure it's empty
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.clear();

    Map<String, JsonElement> result = jsonObject.asMap();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    assertSame(members, result);
  }

  @Test
    @Timeout(8000)
  public void testAsMap_WithMembers() throws Exception {
    // Use reflection to access members and add mock entries
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);

    members.put("key1", element1);
    members.put("key2", element2);

    Map<String, JsonElement> result = jsonObject.asMap();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertSame(element1, result.get("key1"));
    assertSame(element2, result.get("key2"));
    assertSame(members, result);
  }
}