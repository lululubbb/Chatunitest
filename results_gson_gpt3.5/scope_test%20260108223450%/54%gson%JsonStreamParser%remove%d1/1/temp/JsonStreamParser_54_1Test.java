package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonStreamParser;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonStreamParser_54_1Test {

  private JsonStreamParser parserFromString;
  private JsonStreamParser parserFromReader;

  @BeforeEach
  public void setUp() {
    parserFromString = new JsonStreamParser("{}");
    parserFromReader = new JsonStreamParser(new StringReader("{}"));
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException_fromStringConstructor() {
    assertThrows(UnsupportedOperationException.class, () -> parserFromString.remove());
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException_fromReaderConstructor() {
    assertThrows(UnsupportedOperationException.class, () -> parserFromReader.remove());
  }
}