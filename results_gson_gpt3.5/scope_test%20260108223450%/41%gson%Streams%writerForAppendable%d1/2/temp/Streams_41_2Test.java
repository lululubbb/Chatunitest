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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

import org.junit.jupiter.api.Test;

class Streams_41_2Test {

  @Test
    @Timeout(8000)
  void writerForAppendable_withWriter_returnsSameInstance() {
    Writer writer = mock(Writer.class);
    Writer result = Streams.writerForAppendable(writer);
    assertSame(writer, result);
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_withNonWriter_returnsAppendableWriter() throws IOException {
    Appendable appendable = new StringBuilder();
    Writer result = Streams.writerForAppendable(appendable);
    assertNotNull(result);
    assertNotSame(appendable, result);
    // The returned Writer should write to the Appendable
    String testString = "test";
    result.write(testString);
    result.flush();
    assertEquals(testString, appendable.toString());
  }
}