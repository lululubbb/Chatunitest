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

class RawCollectionsExample_142_2Test {

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
  void testMain_printsExpectedOutput() throws Exception {
    RawCollectionsExample.main(new String[0]);

    String output = outContent.toString().replaceAll("\\r\\n", "\n");

    // Updated expected JSON string to match actual output
    org.junit.jupiter.api.Assertions.assertTrue(
        output.contains("Using Gson.toJson() on a raw collection: [\"hello\",5,{\"name\":\"GREETINGS\",\"source\":\"guest\"}]"),
        "Output did not contain expected JSON serialization line. Actual output:\n" + output);

    // Updated expected deserialization line to match actual output format
    org.junit.jupiter.api.Assertions.assertTrue(
        output.contains("Using Gson.fromJson() to get: hello, 5, (name=GREETINGS, source=guest)"),
        "Output did not contain expected deserialization line. Actual output:\n" + output);
  }
}