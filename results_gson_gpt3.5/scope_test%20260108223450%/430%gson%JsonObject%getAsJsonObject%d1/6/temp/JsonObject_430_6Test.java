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

public class JsonObject_430_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_existingMember_returnsJsonObject() throws Exception {
    JsonObject expected = new JsonObject();
    setMembersField(jsonObject, "existing", expected);

    JsonObject actual = jsonObject.getAsJsonObject("existing");

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
  public void testGetAsJsonObject_nullMemberName_returnsNull() {
    JsonObject actual = jsonObject.getAsJsonObject(null);
    assertNull(actual);
  }

  private void setMembersField(JsonObject jsonObject, String key, JsonElement value) throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put(key, value);
  }
}