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
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DateTypeAdapter_296_6Test {

    private DateTypeAdapter dateTypeAdapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() {
        dateTypeAdapter = new DateTypeAdapter();
        // Use a real JsonWriter with StringWriter to verify output
        jsonWriter = new JsonWriter(new StringWriter());
    }

    @Test
    @Timeout(8000)
    void write_nullValue_shouldWriteNull() throws IOException {
        JsonWriter spyWriter = spy(jsonWriter);

        dateTypeAdapter.write(spyWriter, null);

        verify(spyWriter).nullValue();
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_shouldFormatDateAndWriteValue() throws IOException {
        // Prepare a known date
        Date date = new Date(0L); // Epoch 1970-01-01T00:00:00Z

        // We need to get the expected formatted string from the adapter's first DateFormat in the list
        // Use reflection to get the private dateFormats list and its first DateFormat
        DateFormat dateFormat;
        try {
            var dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
            dateFormatsField.setAccessible(true);
            @SuppressWarnings("unchecked")
            var dateFormats = (java.util.List<DateFormat>) dateFormatsField.get(dateTypeAdapter);
            dateFormat = dateFormats.get(0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        String expectedFormattedDate;
        synchronized (dateTypeAdapter) {
            expectedFormattedDate = dateFormat.format(date);
        }

        StringWriter stringWriter = new StringWriter();
        JsonWriter realWriter = new JsonWriter(stringWriter);

        dateTypeAdapter.write(realWriter, date);

        realWriter.flush();
        String jsonOutput = stringWriter.toString();

        // JsonWriter.value(String) writes the string with quotes, verify output contains the formatted string inside quotes
        // e.g. "Jan 1, 1970, 12:00:00 AM"
        // So jsonOutput should be quoted string containing expectedFormattedDate
        // Check output format: it should be the quoted string of expectedFormattedDate
        String expectedJson = "\"" + expectedFormattedDate + "\"";
        org.junit.jupiter.api.Assertions.assertEquals(expectedJson, jsonOutput);
    }
}