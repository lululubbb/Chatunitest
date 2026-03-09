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

public class JsonObject_426_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testHas_ReturnsTrue_WhenMemberExists() throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> membersOriginal = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    LinkedTreeMap<String, JsonElement> membersSpy = spy(membersOriginal);

    // Use doReturn() to stub the method without calling real method
    doReturn(true).when(membersSpy).containsKey("existingKey");

    membersField.set(jsonObject, membersSpy);

    assertTrue(jsonObject.has("existingKey"));
    verify(membersSpy).containsKey("existingKey");
  }

  @Test
    @Timeout(8000)
  public void testHas_ReturnsFalse_WhenMemberDoesNotExist() throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> membersOriginal = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    LinkedTreeMap<String, JsonElement> membersSpy = spy(membersOriginal);

    doReturn(false).when(membersSpy).containsKey("missingKey");

    membersField.set(jsonObject, membersSpy);

    assertFalse(jsonObject.has("missingKey"));
    verify(membersSpy).containsKey("missingKey");
  }

  @Test
    @Timeout(8000)
  public void testHas_ReturnsFalse_WhenMemberNameIsNull() throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);

    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> membersOriginal = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    LinkedTreeMap<String, JsonElement> membersSpy = spy(membersOriginal);

    doReturn(false).when(membersSpy).containsKey(null);

    membersField.set(jsonObject, membersSpy);

    assertFalse(jsonObject.has(null));
    verify(membersSpy).containsKey(null);
  }
}