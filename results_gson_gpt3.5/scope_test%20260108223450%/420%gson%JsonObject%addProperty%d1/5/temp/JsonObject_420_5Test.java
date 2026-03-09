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

public class JsonObject_420_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withTrueBoolean_addsJsonPrimitiveTrue() throws Exception {
    jsonObject.addProperty("key", Boolean.TRUE);

    // Verify add called with correct parameters
    verify(jsonObject).add(eq("key"), argThat(element -> element instanceof JsonPrimitive && Boolean.TRUE.equals(((JsonPrimitive) element).getAsBoolean())));
    // Check internal members map contains key with JsonPrimitive true
    LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
    assertTrue(members.containsKey("key"));
    assertTrue(members.get("key") instanceof JsonPrimitive);
    assertEquals(Boolean.TRUE, ((JsonPrimitive) members.get("key")).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withFalseBoolean_addsJsonPrimitiveFalse() throws Exception {
    jsonObject.addProperty("key", Boolean.FALSE);

    verify(jsonObject).add(eq("key"), argThat(element -> element instanceof JsonPrimitive && Boolean.FALSE.equals(((JsonPrimitive) element).getAsBoolean())));
    LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
    assertTrue(members.containsKey("key"));
    assertTrue(members.get("key") instanceof JsonPrimitive);
    assertEquals(Boolean.FALSE, ((JsonPrimitive) members.get("key")).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullBoolean_addsJsonNullInstance() throws Exception {
    jsonObject.addProperty("key", (Boolean) null);

    verify(jsonObject).add(eq("key"), eq(JsonNull.INSTANCE));
    LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
    assertTrue(members.containsKey("key"));
    assertEquals(JsonNull.INSTANCE, members.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_privateAddMethodAddsToMembers() throws Exception {
    // Use reflection to invoke private add method with a JsonPrimitive
    JsonPrimitive primitive = new JsonPrimitive(true);
    invokeAdd(jsonObject, "testKey", primitive);

    LinkedTreeMap<String, JsonElement> members = getMembersMap(jsonObject);
    assertTrue(members.containsKey("testKey"));
    assertSame(primitive, members.get("testKey"));
  }

  private LinkedTreeMap<String, JsonElement> getMembersMap(JsonObject obj) throws Exception {
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    return (LinkedTreeMap<String, JsonElement>) membersField.get(obj);
  }

  private void invokeAdd(JsonObject obj, String property, JsonElement value) throws Exception {
    var addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);
    addMethod.invoke(obj, property, value);
  }
}