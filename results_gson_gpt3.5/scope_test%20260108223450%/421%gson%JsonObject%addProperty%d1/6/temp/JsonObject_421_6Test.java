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

public class JsonObject_421_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = spy(new JsonObject());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNonNullCharacter_addsCorrectJsonPrimitive() throws Exception {
    Character value = 'a';
    String property = "charProperty";

    // Call the focal method
    jsonObject.addProperty(property, value);

    // Verify that add was called with correct arguments
    verify(jsonObject).add(eq(property), argThat(jsonElement -> {
      if (!(jsonElement instanceof JsonPrimitive)) return false;
      JsonPrimitive primitive = (JsonPrimitive) jsonElement;
      return primitive.getAsCharacter() == value.charValue();
    }));

    // Verify internal members map contains the key and correct value
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement storedElement = members.get(property);
    assertTrue(storedElement instanceof JsonPrimitive);
    JsonPrimitive storedPrimitive = (JsonPrimitive) storedElement;
    assertEquals(value.charValue(), storedPrimitive.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_withNullCharacter_addsJsonNullInstance() throws Exception {
    Character value = null;
    String property = "nullCharProperty";

    // Call the focal method
    jsonObject.addProperty(property, value);

    // Verify that add was called with correct arguments
    verify(jsonObject).add(eq(property), eq(JsonNull.INSTANCE));

    // Verify internal members map contains the key and JsonNull instance
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
  public void testAdd_invokedByAddProperty_addsElementToMembers() throws Exception {
    String property = "testProperty";
    JsonPrimitive primitive = new JsonPrimitive('z');

    // Call addProperty which calls add internally
    jsonObject.addProperty(property, 'z');

    // Verify members map contains the property with correct JsonElement
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(primitive.getAsCharacter(), ((JsonPrimitive) element).getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testAddProperty_privateAddMethodInvocation() throws Exception {
    // Use reflection to invoke private add method directly
    Method addMethod = JsonObject.class.getDeclaredMethod("add", String.class, JsonElement.class);
    addMethod.setAccessible(true);

    String property = "reflectionProperty";
    JsonPrimitive primitive = new JsonPrimitive('x');

    addMethod.invoke(jsonObject, property, primitive);

    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedTreeMap<String, JsonElement> members = (LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);

    assertTrue(members.containsKey(property));
    JsonElement element = members.get(property);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals('x', ((JsonPrimitive) element).getAsCharacter());
  }
}