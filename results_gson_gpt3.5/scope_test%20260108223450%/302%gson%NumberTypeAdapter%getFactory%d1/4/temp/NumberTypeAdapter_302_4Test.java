package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapterFactory;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import org.junit.jupiter.api.Test;

class NumberTypeAdapter_302_4Test {

  @Test
    @Timeout(8000)
  void getFactory_withLazilyParsedNumber_returnsLazilyParsedNumberFactory() {
    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    assertNotNull(factory);
    assertSame(getPrivateStaticField("LAZILY_PARSED_NUMBER_FACTORY"), factory);
  }

  @Test
    @Timeout(8000)
  void getFactory_withCustomStrategy_returnsNewFactory() {
    ToNumberStrategy customStrategy = mock(ToNumberStrategy.class);
    // Ensure customStrategy is not LAZILY_PARSED_NUMBER
    assertNotSame(ToNumberPolicy.LAZILY_PARSED_NUMBER, customStrategy);

    TypeAdapterFactory factory = NumberTypeAdapter.getFactory(customStrategy);
    assertNotNull(factory);
    assertNotSame(getPrivateStaticField("LAZILY_PARSED_NUMBER_FACTORY"), factory);
  }

  private static Object getPrivateStaticField(String fieldName) {
    try {
      var field = NumberTypeAdapter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}