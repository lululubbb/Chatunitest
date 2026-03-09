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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

class DateTypeAdapter_296_5Test {

    private DateTypeAdapter adapter;
    private JsonWriter out;

    @BeforeEach
    void setUp() {
        adapter = new DateTypeAdapter();
        out = mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
        adapter.write(out, null);
        verify(out).nullValue();
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_formatsDateAndWritesValue() throws Exception {
        Date date = new Date();

        // Access private field dateFormats and replace with controlled DateFormat list
        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

        // Replace the list with a mock DateFormat
        DateFormat mockDateFormat = mock(DateFormat.class);
        dateFormats.clear();
        dateFormats.add(mockDateFormat);

        String formattedDate = "formatted-date-string";
        when(mockDateFormat.format(date)).thenReturn(formattedDate);

        adapter.write(out, date);

        InOrder inOrder = inOrder(mockDateFormat, out);
        inOrder.verify(mockDateFormat).format(date);
        inOrder.verify(out).value(formattedDate);
        verifyNoMoreInteractions(out, mockDateFormat);
    }
}