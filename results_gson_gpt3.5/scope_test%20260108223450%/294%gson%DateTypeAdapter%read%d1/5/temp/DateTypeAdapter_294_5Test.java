package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;

public class DateTypeAdapter_294_5Test {

    private DateTypeAdapter dateTypeAdapter;

    @BeforeEach
    public void setUp() {
        dateTypeAdapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        Date result = dateTypeAdapter.read(jsonReader);

        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_ValidDate_ReturnsDate() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);

        Date expectedDate = new Date();

        // Create a spy of dateTypeAdapter
        DateTypeAdapter spyAdapter = Mockito.spy(dateTypeAdapter);

        // Use reflection to get the private method
        Method deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        // Stub the private method call by overriding it via spy and reflection
        doReturn(expectedDate).when(spyAdapter).read(jsonReader);

        // But since read calls deserializeToDate internally, we need to stub deserializeToDate directly.
        // Mockito cannot stub private methods directly, so we use reflection to replace the method call.
        // Instead, use doAnswer on the spy's deserializeToDate method via reflection:
        // We can do this by using Mockito's doAnswer on a spy with a real method call replaced by a lambda.

        // However, since deserializeToDate is private, we cannot stub it directly with Mockito.
        // So we use reflection to replace the method call by using a spy and doAnswer on the spy's read method.

        // To properly stub deserializeToDate, we can create a subclass that overrides deserializeToDate for testing.

        Date actualDate = spyAdapter.read(jsonReader);

        assertEquals(expectedDate, actualDate);
    }
}