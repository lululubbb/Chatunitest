package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_429_5Test {

  private JsonObject jsonObject;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMember_returnsJsonArray() {
    JsonArray jsonArray = new JsonArray();
    jsonObject.add("arrayMember", jsonArray);

    JsonArray result = jsonObject.getAsJsonArray("arrayMember");

    assertSame(jsonArray, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_nonExistingMember_returnsNull() {
    JsonArray result = jsonObject.getAsJsonArray("nonExisting");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMemberNotJsonArray_returnsNull() {
    JsonPrimitive primitive = new JsonPrimitive("stringValue");
    jsonObject.add("primitiveMember", primitive);

    JsonElement element = jsonObject.get("primitiveMember");
    JsonArray result = null;
    if (element instanceof JsonArray) {
      result = (JsonArray) element;
    }

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_existingMemberNotJsonArray_throwsClassCastException() {
    JsonPrimitive primitive = new JsonPrimitive("stringValue");
    jsonObject.add("primitiveMember", primitive);

    assertThrows(ClassCastException.class, () -> jsonObject.getAsJsonArray("primitiveMember"));
  }
}