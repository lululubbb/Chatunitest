package com.google.gson.extras.examples.rawcollections;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

class RawCollectionsExample_142_6Test {

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream outContent;

  private MockedStatic<JsonParser> jsonParserStatic;

  private Object eventInstance;
  private Class<?> eventClass;

  @BeforeEach
  void setUp() throws Exception {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    jsonParserStatic = mockStatic(JsonParser.class);

    // Instead of loading Event class via reflection (which fails), create a dummy Event instance using a minimal implementation

    // Create eventInstance by invoking the constructor of Event from RawCollectionsExample's classloader
    Class<?> rawCollectionsExampleClass = Class.forName("com.google.gson.extras.examples.rawcollections.RawCollectionsExample");
    ClassLoader classLoader = rawCollectionsExampleClass.getClassLoader();

    // Load Event class from the same classloader as RawCollectionsExample
    eventClass = classLoader.loadClass("com.google.gson.extras.examples.rawcollections.Event");
    Constructor<?> constructor = eventClass.getConstructor(String.class, String.class);
    eventInstance = constructor.newInstance("GREETINGS", "guest");
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
    jsonParserStatic.close();
  }

  @Test
    @Timeout(8000)
  void testMain() throws Exception {
    Gson gson = new Gson();

    Collection<Object> collection = new ArrayList<>();
    collection.add("hello");
    collection.add(5);
    collection.add(eventInstance);
    String json = gson.toJson(collection);

    JsonArray jsonArray = new JsonArray();
    jsonArray.add(gson.toJsonTree("hello"));
    jsonArray.add(gson.toJsonTree(5));
    jsonArray.add(gson.toJsonTree(eventInstance));

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.getAsJsonArray()).thenReturn(jsonArray);
    jsonParserStatic.when(() -> JsonParser.parseString(json)).thenReturn(jsonElement);

    RawCollectionsExample.main(new String[0]);

    String output = outContent.toString();

    assert output.contains("Using Gson.toJson() on a raw collection:");
    assert output.contains("[\"hello\",5,{\"type\":\"GREETINGS\",\"guestName\":\"guest\"}]");

    assert output.contains("Using Gson.fromJson() to get: hello, 5,");
    // The event's toString might print the type or the default Object.toString()
    assert output.contains("Event@") || output.contains("GREETINGS");
  }
}