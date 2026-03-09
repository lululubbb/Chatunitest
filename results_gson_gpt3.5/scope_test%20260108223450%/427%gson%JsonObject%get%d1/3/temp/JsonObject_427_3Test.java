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

public class JsonObject_427_3Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGet_existingMember_returnsValue() throws Exception {
    JsonElement element = mock(JsonElement.class);

    // Use reflection to access private 'members' field and put a value
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
    JsonElement result = jsonObject.get("nonExistentKey");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsNull() {
    JsonElement result = jsonObject.get(null);
    assertNull(result);
  }
}