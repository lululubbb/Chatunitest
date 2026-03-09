package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineItem_628_2Test {

  private LineItem lineItem;

  @BeforeEach
  void setUp() {
    lineItem = new LineItem("TestName", 5, 1000L, "USD");
  }

  @Test
    @Timeout(8000)
  void testGetName() {
    assertEquals("TestName", lineItem.getName());
  }
}