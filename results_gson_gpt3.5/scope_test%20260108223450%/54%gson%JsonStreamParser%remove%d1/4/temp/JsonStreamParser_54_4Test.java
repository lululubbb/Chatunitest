package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonStreamParser;
import java.util.Iterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonStreamParser_54_4Test {

  private JsonStreamParser parser;

  @BeforeEach
  public void setUp() {
    parser = new JsonStreamParser("{}");
  }

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    Iterator<?> iterator = parser;
    assertThrows(UnsupportedOperationException.class, iterator::remove);
  }
}