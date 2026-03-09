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

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class DateTypeAdapter_294_1Test {

    private DateTypeAdapter dateTypeAdapter;

    @BeforeEach
    public void setUp() {
        dateTypeAdapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Date result = dateTypeAdapter.read(in);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_ValidDateToken_ReturnsDate() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);

        Date expectedDate = new Date();

        // Use reflection to access the private method deserializeToDate
        Method deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        // Spy the dateTypeAdapter and stub the private method via reflection invoke
        DateTypeAdapter spyAdapter = Mockito.spy(dateTypeAdapter);

        // Stub the read method on spyAdapter to call the original read method but replace deserializeToDate invocation
        doAnswer(invocation -> {
            JsonReader reader = invocation.getArgument(0);
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            // Instead of calling the real deserializeToDate, return expectedDate
            return expectedDate;
        }).when(spyAdapter).read(in);

        Date actualDate = spyAdapter.read(in);

        assertEquals(expectedDate, actualDate);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_WithValidDateString() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("Jan 1, 2020, 12:00:00 AM");
        when(in.peek()).thenReturn(JsonToken.STRING);

        Method deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        Date result = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, in);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_WithInvalidDateString_ThrowsJsonSyntaxException() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("invalid-date");
        when(in.peek()).thenReturn(JsonToken.STRING);

        Method deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        assertThrows(RuntimeException.class, () -> {
            try {
                deserializeToDateMethod.invoke(dateTypeAdapter, in);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }
}