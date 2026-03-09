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
import java.util.Locale;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DateTypeAdapter_296_1Test {
    private DateTypeAdapter dateTypeAdapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        dateTypeAdapter = new DateTypeAdapter();
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNullValue_writesNullValue() throws IOException {
        dateTypeAdapter.write(jsonWriter, null);
        jsonWriter.flush();
        // The JsonWriter writes "null" for null values
        assert stringWriter.toString().equals("null");
    }

    @Test
    @Timeout(8000)
    public void testWrite_withNonNullValue_writesFormattedDate() throws Exception {
        Date date = new Date(0L); // Epoch time
        
        // Access private field dateFormats and set a known DateFormat for predictable output
        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(dateTypeAdapter);
        dateFormats.clear();
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormats.add(simpleDateFormat);

        dateTypeAdapter.write(jsonWriter, date);
        jsonWriter.flush();

        String expected = "\"" + simpleDateFormat.format(date) + "\"";
        String actual = stringWriter.toString();
        assert actual.equals(expected);
    }
}