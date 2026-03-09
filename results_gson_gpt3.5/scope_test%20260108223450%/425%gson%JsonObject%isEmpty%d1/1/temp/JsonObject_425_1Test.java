package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonObject_425_1Test {

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenEmpty() throws Exception {
    JsonObject jsonObject = new JsonObject();
    // Ensure members map is empty
    Field membersField = JsonObject.class.getDeclaredField("members");
    membersField.setAccessible(true);
    Object members = membersField.get(jsonObject);
    // members is LinkedTreeMap which starts empty by default, so no modification needed
    assertTrue(jsonObject.isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testIsEmpty_whenNotEmpty() throws Exception {
    JsonObject jsonObject = new JsonObject();
    // Add a property to make members non-empty
    jsonObject.addProperty("key", "value");
    assertFalse(jsonObject.isEmpty());
  }
}