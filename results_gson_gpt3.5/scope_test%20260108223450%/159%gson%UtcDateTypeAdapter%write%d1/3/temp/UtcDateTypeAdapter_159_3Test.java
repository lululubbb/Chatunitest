package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import static org.mockito.Mockito.*;

import com.google.gson.typeadapters.UtcDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimeZone;

class UtcDateTypeAdapter_159_3Test {

    private UtcDateTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        adapter = new UtcDateTypeAdapter();
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void write_nullDate_shouldWriteNullValue() throws IOException {
        adapter.write(jsonWriter, null);
        String jsonOutput = stringWriter.toString();
        // JsonWriter writes "null" for nullValue()
        // The actual output is "null"
        assert jsonOutput.equals("null");
    }

    @Test
    @Timeout(8000)
    void write_nonNullDate_shouldWriteFormattedValue() throws Exception {
        Date date = new Date(0L); // Epoch

        // Use reflection to invoke private format method to know expected output
        Method formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
        formatMethod.setAccessible(true);
        String expectedValue = (String) formatMethod.invoke(adapter, date, true, TimeZone.getTimeZone("UTC"));

        adapter.write(jsonWriter, date);
        String jsonOutput = stringWriter.toString();

        // JsonWriter.value(String) writes quoted string, so output should be quoted expectedValue
        assert jsonOutput.equals("\"" + expectedValue + "\"");
    }
}