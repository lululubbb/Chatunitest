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

public class JsonObject_427_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() throws Exception {
    jsonObject = new JsonObject();

    // Use reflection to inject a mock LinkedTreeMap into the private final field 'members'
    LinkedTreeMap<String, JsonElement> membersMock = mock(LinkedTreeMap.class);
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    membersField.set(jsonObject, membersMock);
  }

  @Test
    @Timeout(8000)
  public void testGet_existingMember_returnsElement() {
    LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
    JsonElement expectedElement = mock(JsonElement.class);
    when(members.get("key")).thenReturn(expectedElement);

    JsonElement actualElement = jsonObject.get("key");

    assertSame(expectedElement, actualElement);
    verify(members).get("key");
  }

  @Test
    @Timeout(8000)
  public void testGet_nonExistingMember_returnsNull() {
    LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
    when(members.get("absentKey")).thenReturn(null);

    JsonElement actualElement = jsonObject.get("absentKey");

    assertNull(actualElement);
    verify(members).get("absentKey");
  }

  @SuppressWarnings("unchecked")
  private LinkedTreeMap<String, JsonElement> getMembersMap(JsonObject obj) {
    try {
      Field membersField = JsonObject.class.getDeclaredField("members");
      membersField.setAccessible(true);
      return (LinkedTreeMap<String, JsonElement>) membersField.get(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}