package com.google.gson.extras.examples.rawcollections;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.Collection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RawCollectionsExample_142_1Test {

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outContent;

  @BeforeEach
  void setUp() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
    @Timeout(8000)
  void testMain() {
    // Run the main method
    RawCollectionsExample.main(new String[0]);

    String output = outContent.toString();

    // Check that the output contains the expected JSON string and formatted output
    assertTrue(output.contains("Using Gson.toJson() on a raw collection: "));
    assertTrue(output.contains("[\"hello\",5,"));
    assertTrue(output.contains("Using Gson.fromJson() to get: hello, 5, Event{type=GREETINGS, name=guest}"));
    // The last output is printed with printf without newline, so add this check:
    assertTrue(output.trim().startsWith("Using Gson.fromJson() to get:"));
    assertTrue(output.trim().endsWith("Event{type=GREETINGS, name=guest}"));
  }

}