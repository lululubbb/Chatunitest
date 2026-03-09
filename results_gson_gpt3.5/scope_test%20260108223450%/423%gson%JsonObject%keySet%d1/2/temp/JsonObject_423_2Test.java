package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonObject_423_2Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void keySet_emptyMembers_returnsEmptySet() {
    Set<String> keys = jsonObject.keySet();
    assertNotNull(keys);
    assertTrue(keys.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void keySet_withMembers_returnsCorrectKeySet() throws Exception {
    // Use reflection to access private 'members' field and populate it
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    JsonElement mockElement1 = mock(JsonElement.class);
    JsonElement mockElement2 = mock(JsonElement.class);

    members.put("key1", mockElement1);
    members.put("key2", mockElement2);

    Set<String> keys = jsonObject.keySet();

    assertNotNull(keys);
    assertEquals(2, keys.size());
    assertTrue(keys.contains("key1"));
    assertTrue(keys.contains("key2"));
  }
}