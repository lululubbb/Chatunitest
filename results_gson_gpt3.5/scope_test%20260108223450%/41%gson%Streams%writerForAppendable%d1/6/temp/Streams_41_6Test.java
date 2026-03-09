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

class Streams_41_6Test {

  @Test
    @Timeout(8000)
  void writerForAppendable_withWriterAppendable_returnsSameInstance() {
    Writer writerMock = mock(Writer.class);
    Writer result = Streams.writerForAppendable(writerMock);
    assertSame(writerMock, result);
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_withNonWriterAppendable_returnsAppendableWriter() throws IOException {
    Appendable appendable = new StringBuilder();
    Writer writer = Streams.writerForAppendable(appendable);
    assertNotNull(writer);
    assertNotSame(appendable, writer);
    // The returned writer should write to the given appendable
    String testString = "test";
    writer.write(testString);
    writer.flush();
    assertEquals(testString, appendable.toString());
  }
}