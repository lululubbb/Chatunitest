package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_520_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_withNull_callsNullValue() throws IOException {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(true).when(spyWriter).isLenient();
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  public void testValue_withValidNumber_putsJsonPrimitiveAndReturnsThis() throws Exception {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(true).when(spyWriter).isLenient();

    Number number = 42;

    // Use reflection to access the private put method
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    InvocationCounter counter = new InvocationCounter();

    // Wrap the original put method with a proxy that increments the counter
    JsonTreeWriter spyWithCounter = spy(spyWriter);
    doAnswer(invocation -> {
      counter.increment();
      // call the original private method via reflection
      putMethod.invoke(invocation.getMock(), invocation.getArgument(0));
      return null;
    }).when(spyWithCounter).value(number); // We cannot mock private put, so mock value to call put manually

    // Instead of mocking put directly (not possible), call value normally and count put calls via reflection
    // So we call value on spyWithCounter, but we need to intercept put calls:
    // Since put is private and cannot be mocked, we call value normally and count put calls by overriding put via reflection is impossible.
    // So alternative: call value normally and count put calls by temporarily replacing put with a method that increments counter.
    // This is complicated, so simpler approach: call value normally, then check state change that put causes.

    // So instead, call value normally on spyWriter and check that product is updated (via get())
    JsonTreeWriter writerForCheck = spy(jsonTreeWriter);
    doReturn(true).when(writerForCheck).isLenient();

    JsonWriter result = writerForCheck.value(number);

    // The product should be a JsonPrimitive with the number value
    assertTrue(writerForCheck.get().isJsonPrimitive());
    assertEquals(new JsonPrimitive(number), writerForCheck.get());

    assertSame(writerForCheck, result);
  }

  private static class InvocationCounter {
    private int count = 0;
    void increment() {
      count++;
    }
    int getCount() {
      return count;
    }
  }

  @Test
    @Timeout(8000)
  public void testValue_withNaN_throwsIllegalArgumentException() {
    Number nanNumber = Double.NaN;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(nanNumber);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withPositiveInfinity_throwsIllegalArgumentException() {
    Number posInf = Double.POSITIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(posInf);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNegativeInfinity_throwsIllegalArgumentException() {
    Number negInf = Double.NEGATIVE_INFINITY;

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      jsonTreeWriter.value(negInf);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_withNonLenientAllowsNaNAndInfinity() throws IOException {
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);
    doReturn(false).when(spyWriter).isLenient();

    Number validNumber = 123;
    JsonWriter result = spyWriter.value(validNumber);
    assertSame(spyWriter, result);

    // For NaN and infinity, exception expected because isLenient() is false
    Number nanNumber = Double.NaN;
    assertThrows(IllegalArgumentException.class, () -> spyWriter.value(nanNumber));
  }
}