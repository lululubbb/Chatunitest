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
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_513_5Test {

  @Test
    @Timeout(8000)
  public void jsonValue_throwsUnsupportedOperationException() {
    JsonTreeWriter writer = new JsonTreeWriter();
    assertThrows(UnsupportedOperationException.class, () -> writer.jsonValue("anyValue"));
  }
}