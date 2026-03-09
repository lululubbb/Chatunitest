package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonObject_427_2Test {

  private JsonObject jsonObject;
  private JsonElement mockElement;

  @BeforeEach
  public void setUp() {
    jsonObject = new JsonObject();
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testGet_existingMember_returnsElement() {
    jsonObject.add("key", mockElement);
    JsonElement result = jsonObject.get("key");
    assertSame(mockElement, result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nonExistingMember_returnsNull() {
    JsonElement result = jsonObject.get("nonexistent");
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testGet_nullKey_returnsNull() {
    JsonElement result = jsonObject.get(null);
    assertNull(result);
  }
}