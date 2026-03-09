package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Field;

public class JsonObject_429_6Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void getAsJsonArray_existingMember_returnsJsonArray() throws Exception {
    JsonArray jsonArray = new JsonArray();
    // Use reflection to set the private members field
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("arrayMember", jsonArray);

    JsonArray result = jsonObject.getAsJsonArray("arrayMember");
    assertSame(jsonArray, result);
  }

  @Test
    @Timeout(8000)
  public void getAsJsonArray_nonExistingMember_returnsNull() {
    JsonArray result = jsonObject.getAsJsonArray("missingMember");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void getAsJsonArray_memberIsNotJsonArray_returnsClassCastException() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("value");
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    com.google.gson.internal.LinkedTreeMap<String, JsonElement> members =
        (com.google.gson.internal.LinkedTreeMap<String, JsonElement>) membersField.get(jsonObject);
    members.put("primitiveMember", jsonPrimitive);

    assertThrows(ClassCastException.class, () -> {
      jsonObject.getAsJsonArray("primitiveMember");
    });
  }
}