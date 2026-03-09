package com.google.gson.extras.examples.rawcollections;
import org.junit.jupiter.api.Timeout;
import java.util.ArrayList;
import java.util.Collection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RawCollectionsExample_142_4Test {

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outContent;

  @BeforeEach
  public void setUp() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  public void tearDown() {
    System.setOut(originalOut);
  }

  @Test
    @Timeout(8000)
  public void testMain() {
    RawCollectionsExample.main(new String[0]);
    String output = outContent.toString();

    // Check that the output contains the expected parts
    assertTrue(output.contains("Using Gson.toJson() on a raw collection:"));
    assertTrue(output.contains("\"hello\""));
    assertTrue(output.contains("5"));
    assertTrue(output.contains("GREETINGS"));
    assertTrue(output.contains("guest"));

    // The last line uses printf without a newline, so trim and check contains without trailing newline issues
    // Adjusted expected string to match the toString() format of Event (with single quotes)
    assertTrue(output.trim().contains("Using Gson.fromJson() to get: hello, 5, Event{type='GREETINGS', name='guest'}"));
  }
}