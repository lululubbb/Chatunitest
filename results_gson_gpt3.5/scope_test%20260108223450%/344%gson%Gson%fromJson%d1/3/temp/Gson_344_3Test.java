package com.google.gson;
import org.junit.jupiter.api.Timeout;
package test.com.google.gson;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonAndType() {
    String json = "\"hello\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    try (MockedStatic<TypeToken> mockedTypeToken = Mockito.mockStatic(TypeToken.class)) {
      mockedTypeToken.when(() -> TypeToken.get(String.class)).thenReturn(typeToken);
      // Spy on Gson to mock fromJson(String, TypeToken)
      Gson spyGson = Mockito.spy(gson);
      String expected = "hello";
      doReturn(expected).when(spyGson).fromJson(json, typeToken);

      String actual = spyGson.fromJson(json, String.class);
      assertEquals(expected, actual);
      verify(spyGson).fromJson(json, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJson() {
    String json = null;
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    try (MockedStatic<TypeToken> mockedTypeToken = Mockito.mockStatic(TypeToken.class)) {
      mockedTypeToken.when(() -> TypeToken.get(Object.class)).thenReturn(typeToken);
      Gson spyGson = Mockito.spy(gson);
      doReturn(null).when(spyGson).fromJson(json, typeToken);

      Object actual = spyGson.fromJson(json, Object.class);
      assertNull(actual);
      verify(spyGson).fromJson(json, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withInvalidJson_throwsJsonSyntaxException() {
    String invalidJson = "{ invalid json }";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    try (MockedStatic<TypeToken> mockedTypeToken = Mockito.mockStatic(TypeToken.class)) {
      mockedTypeToken.when(() -> TypeToken.get(String.class)).thenReturn(typeToken);
      Gson spyGson = Mockito.spy(gson);
      doThrow(new JsonSyntaxException("Malformed JSON")).when(spyGson).fromJson(invalidJson, typeToken);

      assertThrows(JsonSyntaxException.class, () -> spyGson.fromJson(invalidJson, String.class));
      verify(spyGson).fromJson(invalidJson, typeToken);
    }
  }

  @Test
    @Timeout(8000)
  public void testFromJson_reflectionInvokePrivateFromJson_StringTypeToken() throws Exception {
    // Use reflection to invoke private fromJson(String, TypeToken)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", String.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    String json = "\"test\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // Because the method is public, this is just to demonstrate reflection invocation
    Object result = fromJsonMethod.invoke(gson, json, typeToken);
    assertEquals("test", result);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_reflectionInvokePrivateFromJson_StringTypeToken_nullJson() throws Exception {
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", String.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Object result = fromJsonMethod.invoke(gson, (String) null, TypeToken.get(Object.class));
    assertNull(result);
  }
}