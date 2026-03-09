package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonObject_426_1Test {

  private JsonObject jsonObject;

  @BeforeEach
  void setUp() {
    jsonObject = new JsonObject();
  }

  @Test
    @Timeout(8000)
  void has_returnsFalse_whenMemberNotPresent() {
    assertFalse(jsonObject.has("missing"));
  }

  @Test
    @Timeout(8000)
  void has_returnsTrue_whenMemberPresent() {
    JsonElement element = mock(JsonElement.class);
    jsonObject.add("key", element);
    assertTrue(jsonObject.has("key"));
  }

  @Test
    @Timeout(8000)
  void has_returnsFalse_forNullKey() {
    assertFalse(jsonObject.has(null));
  }
}