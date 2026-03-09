package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonStreamParser_54_5Test {

  @Test
    @Timeout(8000)
  public void testRemove_throwsUnsupportedOperationException() {
    JsonStreamParser parser = new JsonStreamParser("[]");
    assertThrows(UnsupportedOperationException.class, parser::remove);
  }
}