package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

class DefaultDateTypeAdapter_599_6Test {
    private DefaultDateTypeAdapter<Date> adapter;
    private DateType<Date> dateType;

    @BeforeEach
    void setUp() throws Exception {
        // Implement the abstract method deserialize(Date) to fix compilation error
        dateType = new DateType<Date>(Date.class) {
            @Override
            public Date deserialize(Date date) {
                return date;
            }
        };
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DateType.class, int.class, int.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(dateType, DateFormat.SHORT, DateFormat.SHORT);
    }

    @Test
    @Timeout(8000)
    void testWrite_nullValue_writesNull() throws IOException {
        JsonWriter writer = mock(JsonWriter.class);
        adapter.write(writer, null);
        verify(writer).nullValue();
        verifyNoMoreInteractions(writer);
    }

    @Test
    @Timeout(8000)
    void testWrite_validDate_writesFormattedString() throws IOException, ParseException {
        JsonWriter writer = mock(JsonWriter.class);
        Date now = new Date();
        adapter.write(writer, now);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(writer).value(captor.capture());
        String formatted = captor.getValue();
        assertNotNull(formatted);
        // The formatted string should parse back to a date equal to original date ignoring milliseconds
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        Date parsed = df.parse(formatted);
        assertNotNull(parsed);
    }

    @Test
    @Timeout(8000)
    void testRead_nullJson_returnsNull() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();
        Date result = adapter.read(reader);
        assertNull(result);
        verify(reader).nextNull();
    }

    @Test
    @Timeout(8000)
    void testRead_validDateString_returnsDate() throws Exception {
        Date now = new Date();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        String formatted = df.format(now);

        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(formatted);

        Date result = adapter.read(reader);
        assertNotNull(result);
        // Dates should be equal ignoring milliseconds differences
        assertEquals(df.format(result), df.format(now));
    }

    @Test
    @Timeout(8000)
    void testRead_invalidDateString_throwsJsonSyntaxException() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("invalid date");

        assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_withVariousFormats() throws Exception {
        Method deserializeToDate = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDate.setAccessible(true);

        Date now = new Date();
        DateFormat dfUS = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        String formattedUS = dfUS.format(now);

        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(formattedUS);

        Date result = (Date) deserializeToDate.invoke(adapter, reader);
        assertNotNull(result);
        assertEquals(dfUS.format(result), dfUS.format(now));
    }

    @Test
    @Timeout(8000)
    void testToString_containsClassName() {
        String toString = adapter.toString();
        assertTrue(toString.contains("DefaultDateTypeAdapter"));
    }
}