package com.google.gson.extras.examples.rawcollections;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RawCollectionsExample_142_5Test {

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
  void testMain_outputAndDeserialization() {
    // Call the main method
    RawCollectionsExample.main(new String[0]);

    String output = outContent.toString();

    // Verify the first print line contains expected json string
    // It should contain the string "hello", number 5, and event type and guest
    assert output.contains("Using Gson.toJson() on a raw collection:");
    assert output.contains("\"hello\"");
    assert output.contains("5");
    assert output.contains("GREETINGS");
    assert output.contains("guest");

    // Verify the second print line contains the formatted output with the values
    assert output.contains("Using Gson.fromJson() to get: hello, 5, Event");
  }
}