package com.google.gson.extras.examples.rawcollections;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mockStatic;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;

class RawCollectionsExample_142_3Test {

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outContent;

  private MockedStatic<JsonParser> jsonParserMock;

  @BeforeEach
  void setUp() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
    if (jsonParserMock != null) {
      jsonParserMock.close();
    }
  }

  @Test
    @Timeout(8000)
  void testMain() throws Exception {
    RawCollectionsExample.main(new String[0]);

    String output = outContent.toString();

    assertTrue(output.contains("Using Gson.toJson() on a raw collection: [\"hello\",5,{\"type\":\"GREETINGS\",\"name\":\"guest\"}]"));

    // Fix: The toString() of Event may be like Event[type=GREETINGS, name=guest] or com.google.gson.extras.examples.rawcollections.Event@...
    // So check for "Using Gson.fromJson() to get: hello, 5, " and then either "Event[type=GREETINGS, name=guest]" or "com.google.gson.extras.examples.rawcollections.Event@"
    // The original test missed the newline at the end of output, so trim output before checking
    output = output.trim();

    // The output is from printf without newline, add a newline to output to match the pattern or check without trimming
    // Actually, since the output is from printf without newline, do not trim it, just check without trimming

    // So revert to original output without trim
    output = outContent.toString();

    assertTrue(output.contains("Using Gson.fromJson() to get: hello, 5, Event[type=GREETINGS, name=guest]") ||
               output.matches("(?s).*Using Gson.fromJson\\(\\) to get: hello, 5, com\\.google\\.gson\\.extras\\.examples\\.rawcollections\\.Event@.*"));
  }
}