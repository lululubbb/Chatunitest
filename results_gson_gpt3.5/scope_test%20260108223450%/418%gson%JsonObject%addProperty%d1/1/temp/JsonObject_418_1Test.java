package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_418_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullValue_invokesAddWithJsonPrimitive() throws Exception {
    String property = "key";
    String value = "value";

    // Call the focal method
    jsonObject.addProperty(property, value);

    // Verify that add was called with property and a JsonPrimitive wrapping the value
    verify(jsonObject).add(eq(property), argThat(element -> element instanceof JsonPrimitive && ((JsonPrimitive) element).getAsString().equals(value)));

    // Also verify the internal map contains the correct entry
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    assertTrue(members.containsKey(property));
    JsonElement storedElement = members.get(property);
    assertTrue(storedElement instanceof JsonPrimitive);
    assertEquals(value, ((JsonPrimitive) storedElement).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullValue_invokesAddWithJsonNullInstance() throws Exception {
    String property = "key";
    String value = null;

    // Call the focal method
    jsonObject.addProperty(property, value);

    // Verify that add was called with property and JsonNull.INSTANCE
    verify(jsonObject).add(eq(property), eq(JsonNull.INSTANCE));

    // Also verify the internal map contains the correct entry
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    assertTrue(members.containsKey(property));
    JsonElement storedElement = members.get(property);
    assertSame(JsonNull.INSTANCE, storedElement);
  }

  @Test
    @Timeout(8000)
  public void testAdd_privateMethodAddsToMembersMap() throws Exception {
    String property = "privateKey";
    JsonPrimitive value = new JsonPrimitive("privateValue");

    // Use reflection to get private add method
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    // Invoke add directly
    addMethod.invoke(jsonObject, property, value);

    // Verify the internal map contains the entry
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    assertTrue(members.containsKey(property));
    assertEquals(value, members.get(property));
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_multipleCalls_overwritesValue() {
    String property = "key";

    jsonObject.addProperty(property, "first");
    jsonObject.addProperty(property, "second");

    JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(property);
    assertNotNull(primitive);
    assertEquals("second", primitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_emptyStringValue() {
    String property = "emptyKey";
    String value = "";

    jsonObject.addProperty(property, value);

    JsonPrimitive primitive = jsonObject.getAsJsonPrimitive(property);
    assertNotNull(primitive);
    assertEquals(value, primitive.getAsString());
  }

}