package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;

class SqlDateTypeAdapter_404_4Test {

    private SqlDateTypeAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        // SqlDateTypeAdapter has a private constructor, instantiate via reflection
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        // Fix the internal format field to match the test input format exactly with Locale.US
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", Locale.US));
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        // nextNull() should be called
        doNothing().when(in).nextNull();

        java.sql.Date result = adapter.read(in);

        assertNull(result);
        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextNull();
        verifyNoMoreInteractions(in);
    }

    @Test
    @Timeout(8000)
    void read_shouldParseValidDateString() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("Jan 2, 2020");
        when(in.getPreviousPath()).thenReturn(null);

        java.sql.Date result = adapter.read(in);

        assertNotNull(result);
        // The value should correspond to Jan 2, 2020
        // Use SimpleDateFormat to parse expected date for comparison
        var sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        java.util.Date expectedUtilDate = sdf.parse("Jan 2, 2020");
        java.sql.Date expectedSqlDate = new java.sql.Date(expectedUtilDate.getTime());
        assertEquals(expectedSqlDate, result);

        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextString();
        verifyNoMoreInteractions(in);
    }

    @Test
    @Timeout(8000)
    void read_shouldThrowJsonSyntaxException_whenParseExceptionOccurs() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String badDateString = "not a date";
        when(in.nextString()).thenReturn(badDateString);
        String fakePath = "$.date";
        when(in.getPreviousPath()).thenReturn(fakePath);

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
        String expectedMessage = "Failed parsing '" + badDateString + "' as SQL Date; at path " + fakePath;
        assertTrue(ex.getMessage().contains(expectedMessage));
        assertNotNull(ex.getCause());
        assertEquals(java.text.ParseException.class, ex.getCause().getClass());

        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextString();
        inOrder.verify(in).getPreviousPath();
        verifyNoMoreInteractions(in);
    }
}