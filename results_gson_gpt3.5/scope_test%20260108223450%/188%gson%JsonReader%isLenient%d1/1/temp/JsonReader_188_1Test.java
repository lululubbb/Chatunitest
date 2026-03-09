package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.StringReader;

public class JsonReader_188_1Test {

  @Test
    @Timeout(8000)
  public void testIsLenient_DefaultIsFalse() {
    JsonReader reader = new JsonReader(new StringReader(""));
    assertFalse(reader.isLenient());
  }

  @Test
    @Timeout(8000)
  public void testIsLenient_SetLenientTrue() {
    JsonReader reader = new JsonReader(new StringReader(""));
    reader.setLenient(true);
    assertTrue(reader.isLenient());
  }

  @Test
    @Timeout(8000)
  public void testIsLenient_SetLenientFalse() {
    JsonReader reader = new JsonReader(new StringReader(""));
    reader.setLenient(true);
    reader.setLenient(false);
    assertFalse(reader.isLenient());
  }
}