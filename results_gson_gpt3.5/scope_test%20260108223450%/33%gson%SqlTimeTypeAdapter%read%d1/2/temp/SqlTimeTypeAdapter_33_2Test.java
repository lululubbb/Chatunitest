package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class SqlTimeTypeAdapter_33_2Test {

    private SqlTimeTypeAdapter adapter;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        // Override the private final DateFormat field 'format' to a lenient SimpleDateFormat with correct pattern and Locale.US
        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        sdf.setLenient(true);
        formatField.set(adapter, sdf);
    }

    @Test
    @Timeout(8000)
    void read_ShouldReturnNull_WhenJsonTokenIsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Time result = adapter.read(in);

        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_ShouldParseAndReturnTime_WhenJsonTokenIsStringAndParseSucceeds() throws IOException, NoSuchFieldException, IllegalAccessException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String timeString = "12:34:56 PM";
        when(in.nextString()).thenReturn(timeString);
        when(in.getPreviousPath()).thenReturn(null);

        // Replace adapter's format with one that matches timeString pattern and Locale.US
        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        sdf.setLenient(true);
        formatField.set(adapter, sdf);

        Time result = adapter.read(in);

        assertNotNull(result);
        // Validate the time matches the parsed string
        SimpleDateFormat sdfCheck = new SimpleDateFormat("hh:mm:ss a", Locale.US);
        Date expectedDate;
        try {
            expectedDate = sdfCheck.parse(timeString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(expectedDate.getTime(), result.getTime());
    }

    @Test
    @Timeout(8000)
    void read_ShouldThrowJsonSyntaxException_WhenParseExceptionOccurs() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String invalidTimeString = "invalid-time-format";
        when(in.nextString()).thenReturn(invalidTimeString);
        when(in.getPreviousPath()).thenReturn("$.time");

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
        String expectedMessage = "Failed parsing '" + invalidTimeString + "' as SQL Time; at path $.time";
        assertTrue(thrown.getMessage().contains(expectedMessage));
        assertNotNull(thrown.getCause());
        assertEquals(java.text.ParseException.class, thrown.getCause().getClass());
    }
}