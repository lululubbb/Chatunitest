package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class JsonObject_427_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGet_existingMember_returnsElement() throws Exception {
    JsonElement element = mock(JsonElement.class);
    // Use reflection to access private field 'members'
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    members.put("key1", element);

    JsonElement result = jsonObject.get("key1");
    assertSame(element, result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nonExistingMember_returnsNull() {
    JsonElement result = jsonObject.get("nonExistingKey");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsNull() {
    // LinkedTreeMap permits null keys, but Gson's JsonObject usage might not
    JsonElement result = jsonObject.get(null);
    assertNull(result);
  }
}