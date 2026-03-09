package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_420_4Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withBooleanTrue() throws Exception {
    jsonObject.addProperty("keyTrue", Boolean.TRUE);

    // Verify add called with correct parameters
    verify(jsonObject).add(eq("keyTrue"), any(JsonPrimitive.class));
    JsonElement element = jsonObject.get("keyTrue");
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(Boolean.TRUE, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withBooleanFalse() throws Exception {
    jsonObject.addProperty("keyFalse", Boolean.FALSE);

    verify(jsonObject).add(eq("keyFalse"), any(JsonPrimitive.class));
    JsonElement element = jsonObject.get("keyFalse");
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(Boolean.FALSE, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withBooleanNull() throws Exception {
    jsonObject.addProperty("keyNull", (Boolean) null);

    verify(jsonObject).add(eq("keyNull"), eq(JsonNull.INSTANCE));
    JsonElement element = jsonObject.get("keyNull");
    assertNotNull(element);
    assertSame(JsonNull.INSTANCE, element);
  }

  @Test
    @Timeout(8000)
  public void testAdd_privateMethod_invocation() throws Exception {
    // Using reflection to invoke private add(String, JsonElement)
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    JsonPrimitive primitive = new JsonPrimitive(true);
    addMethod.invoke(jsonObject, "reflectKey", primitive);

    JsonElement element = jsonObject.get("reflectKey");
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals(true, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testMembersField_reflectiveCheck() throws Exception {
    // Check that members map contains correct entries after addProperty
    jsonObject.addProperty("reflectiveKey", Boolean.TRUE);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members =
        (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey("reflectiveKey"));
    assertTrue(members.get("reflectiveKey").isJsonPrimitive());
    assertEquals(Boolean.TRUE, ((JsonPrimitive) members.get("reflectiveKey")).getAsBoolean());
  }
}