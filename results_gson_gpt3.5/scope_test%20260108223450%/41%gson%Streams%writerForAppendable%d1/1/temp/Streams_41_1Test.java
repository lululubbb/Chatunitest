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

class Streams_41_1Test {

  @Test
    @Timeout(8000)
  void writerForAppendable_withWriterInstance_returnsSameInstance() {
    Writer writerMock = mock(Writer.class);
    Writer result = Streams.writerForAppendable(writerMock);
    assertSame(writerMock, result);
  }

  @Test
    @Timeout(8000)
  void writerForAppendable_withNonWriterAppendable_returnsAppendableWriter() throws IOException {
    Appendable appendable = new StringBuilder();
    Writer result = Streams.writerForAppendable(appendable);
    assertNotNull(result);
    // The returned Writer should not be the same as the appendable (since appendable is not a Writer)
    assertNotSame(appendable, result);

    // Using reflection to invoke AppendableWriter's write method to verify it delegates to Appendable
    try {
      result.write('a');
      result.flush();
      // The appendable should have 'a' appended
      assertEquals("a", appendable.toString());
    } finally {
      result.close();
    }
  }
}