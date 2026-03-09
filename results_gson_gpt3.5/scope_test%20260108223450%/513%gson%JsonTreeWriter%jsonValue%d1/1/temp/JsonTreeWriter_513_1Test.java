package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_513_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void jsonValue_shouldThrowUnsupportedOperationException() {
    assertThrows(UnsupportedOperationException.class, () -> jsonTreeWriter.jsonValue("test"));
  }
}