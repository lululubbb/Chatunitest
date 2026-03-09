package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class TypeAdapterToJsonTest {

  static class TestTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) throws IOException {
      if (value == null) {
        out.nullValue();
      } else {
        out.value(value);
      }
    }

    @Override
    public String read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void toJson_callsWriteWithJsonWriter() throws Exception {
    TestTypeAdapter adapter = new TestTypeAdapter();
    StringWriter stringWriter = new StringWriter();

    adapter.toJson(stringWriter, "testValue");

    String jsonOutput = stringWriter.toString();
    // The output should be a JSON string with "testValue"
    // Because JsonWriter.value(String) writes JSON string with quotes
    assert jsonOutput.equals("\"testValue\"");
  }

  @Test
    @Timeout(8000)
  void toJson_callsWriteWithNullValue() throws Exception {
    TestTypeAdapter adapter = new TestTypeAdapter();
    StringWriter stringWriter = new StringWriter();

    adapter.toJson(stringWriter, null);

    String jsonOutput = stringWriter.toString();
    // Null value should be serialized as "null"
    assert jsonOutput.equals("null");
  }

  @Test
    @Timeout(8000)
  void toJson_invokesWriteMethodWithSpy() throws Exception {
    TestTypeAdapter realAdapter = new TestTypeAdapter();
    TestTypeAdapter spyAdapter = spy(realAdapter);

    StringWriter stringWriter = new StringWriter();
    spyAdapter.toJson(stringWriter, "reflectionTest");

    verify(spyAdapter, times(1)).write(any(JsonWriter.class), eq("reflectionTest"));
  }
}