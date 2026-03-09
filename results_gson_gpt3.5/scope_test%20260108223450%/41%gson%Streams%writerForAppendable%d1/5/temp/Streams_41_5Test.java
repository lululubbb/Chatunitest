package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Writer;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Streams_41_5Test {

  @Test
    @Timeout(8000)
  void writerForAppendable_withWriter_returnsSameInstance() {
    Writer mockWriter = mock(Writer.class);
    Writer result = Streams.writerForAppendable(mockWriter);
    assertSame(mockWriter, result);
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_withAppendable_returnsAppendableWriter() throws Exception {
    Appendable appendable = new StringBuilder();
    Writer result = Streams.writerForAppendable(appendable);
    assertNotNull(result);
    assertNotSame(appendable, result);
    assertEquals("com.google.gson.internal.Streams$AppendableWriter", result.getClass().getName());

    // Use reflection to invoke private method (if needed, here we check class is correct)
    // No private method to invoke in the focal method, but demonstrating reflection usage:
    Method writerForAppendableMethod = Streams.class.getDeclaredMethod("writerForAppendable", Appendable.class);
    writerForAppendableMethod.setAccessible(true);
    Writer reflectedResult = (Writer) writerForAppendableMethod.invoke(null, appendable);
    assertNotNull(reflectedResult);
    assertEquals(result.getClass(), reflectedResult.getClass());
  }
}