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

class JsonObjectEqualsTest {

  private JsonObject jsonObject;
  private LinkedTreeMap<String, JsonElement> membersMock;

  @BeforeEach
  public void setUp() throws Exception {
    jsonObject = new JsonObject();

    // Use reflection to set the private final members field to a mock
    membersMock = mock(LinkedTreeMap.class);
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    membersField.set(jsonObject, membersMock);
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameReference() {
    assertTrue(jsonObject.equals(jsonObject));
  }

  @Test
    @Timeout(8000)
  public void testEquals_nullObject() {
    assertFalse(jsonObject.equals(null));
  }

  @Test
    @Timeout(8000)
  public void testEquals_notInstanceOfJsonObject() {
    Object other = new Object();
    assertFalse(jsonObject.equals(other));
  }

  @Test
    @Timeout(8000)
  public void testEquals_differentMembers() {
    JsonObject other = new JsonObject();
    // mock members of other
    LinkedTreeMap<String, JsonElement> otherMembersMock = mock(LinkedTreeMap.class);
    setMembersField(other, otherMembersMock);

    when(otherMembersMock.equals(membersMock)).thenReturn(false);

    assertFalse(jsonObject.equals(other));

    verify(otherMembersMock).equals(membersMock);
  }

  @Test
    @Timeout(8000)
  public void testEquals_sameMembers() {
    JsonObject other = new JsonObject();
    // mock members of other
    LinkedTreeMap<String, JsonElement> otherMembersMock = mock(LinkedTreeMap.class);
    setMembersField(other, otherMembersMock);

    when(otherMembersMock.equals(membersMock)).thenReturn(true);

    assertTrue(jsonObject.equals(other));

    verify(otherMembersMock).equals(membersMock);
  }

  private void setMembersField(JsonObject obj, LinkedTreeMap<String, JsonElement> members) {
    try {
      Field membersField = JsonObject.class.getDeclaredField("members");
      membersField.setAccessible(true);
      membersField.set(obj, members);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}