package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Excluder_446_6Test {

  @Test
    @Timeout(8000)
  void testExcludeFieldsWithoutExposeAnnotation_returnsCloneWithRequireExposeTrue() throws Exception {
    Excluder original = spy(new Excluder());
    Excluder clone = new Excluder();
    doReturn(clone).when(original).clone();

    Excluder result = original.excludeFieldsWithoutExposeAnnotation();

    assertNotNull(result);
    assertSame(clone, result);
    // Use reflection to check private boolean requireExpose
    Field field = Excluder.class.getDeclaredField("requireExpose");
    field.setAccessible(true);
    boolean requireExpose = (boolean) field.get(result);
    assertTrue(requireExpose);
  }
}