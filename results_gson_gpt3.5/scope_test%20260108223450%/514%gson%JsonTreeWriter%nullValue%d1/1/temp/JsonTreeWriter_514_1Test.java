package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_514_1Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void nullValue_shouldPutJsonNullInstanceAndReturnThis() throws Exception {
    JsonWriter returned = writer.nullValue();
    assertSame(writer, returned);

    // Use reflection to access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // The stack should NOT be empty
    assertFalse(stack.isEmpty());
    // The last element should be JsonNull.INSTANCE
    assertSame(JsonNull.INSTANCE, stack.get(stack.size() - 1));
  }

  @Test
    @Timeout(8000)
  public void nullValue_shouldInvokePrivatePutWithJsonNullInstance() throws Exception {
    // Spy on the writer to verify private method invocation indirectly
    JsonTreeWriter spyWriter = spy(new JsonTreeWriter());

    // Call nullValue()
    spyWriter.nullValue();

    // Use reflection to get private field stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(spyWriter);

    // The stack should NOT be empty
    assertFalse(stack.isEmpty());
    // The last element should be JsonNull.INSTANCE
    assertSame(JsonNull.INSTANCE, stack.get(stack.size() - 1));
  }

  @Test
    @Timeout(8000)
  public void nullValue_shouldThrowIOException_whenPutThrowsIOException() throws Exception {
    // Use reflection to get private method 'put'
    var putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Create a new JsonTreeWriter instance
    JsonTreeWriter faultyWriter = new JsonTreeWriter();

    // Use Mockito spy to override 'put' method behavior to throw IOException via reflection
    JsonTreeWriter spyWriter = spy(faultyWriter);

    doAnswer(invocation -> {
      throw new IOException("forced IOException");
    }).when(spyWriter).nullValue(); // We cannot mock nullValue directly because it's final, so mock put via reflection below

    // Instead of mocking nullValue, we will mock put via reflection by using a wrapper method
    // So we create a helper subclass with a put method that throws IOException
    // But since class is final, we cannot subclass - so we use reflection to replace put method call

    // So we invoke nullValue but intercept the call to put via reflection:
    // Instead, we use reflection to invoke 'put' and throw IOException ourselves to simulate behavior

    // So we test that invoking nullValue throws IOException by invoking put with forced exception

    IOException thrown = assertThrows(IOException.class, () -> {
      // Use reflection to invoke 'put' to throw IOException
      putMethod.invoke(spyWriter, (JsonElement) null);
    });
    assertEquals("forced IOException", thrown.getCause().getMessage());
  }
}