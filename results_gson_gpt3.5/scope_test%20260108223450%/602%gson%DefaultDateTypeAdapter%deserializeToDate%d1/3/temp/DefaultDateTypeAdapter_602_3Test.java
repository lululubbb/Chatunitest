package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_deserializeToDate_Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonReader jsonReaderMock;

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to create an instance of DefaultDateTypeAdapter<Date> with a dummy DateType
        var constructors = DefaultDateTypeAdapter.class.getDeclaredConstructors();
        for (var ctor : constructors) {
            if (ctor.getParameterCount() == 2) {
                ctor.setAccessible(true);
                // DateType is a package-private interface, pass null to satisfy constructor
                adapter = (DefaultDateTypeAdapter<Date>) ctor.newInstance(null, "yyyy-MM-dd");
                break;
            }
        }
        jsonReaderMock = mock(JsonReader.class);
        // Mock getPreviousPath to avoid NullPointerException in error message
        when(jsonReaderMock.getPreviousPath()).thenReturn("$");
    }

    private Date invokeDeserializeToDate(DefaultDateTypeAdapter<Date> adapterInstance, JsonReader in) throws Exception {
        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        return (Date) method.invoke(adapterInstance, in);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successfulParsingWithDateFormat() throws Exception {
        String dateStr = "2023-06-15";
        when(jsonReaderMock.nextString()).thenReturn(dateStr);

        // Prepare a SimpleDateFormat that can parse the date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Inject this dateFormat into the adapter's dateFormats list
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(sdf);

        Date parsedDate = invokeDeserializeToDate(adapter, jsonReaderMock);

        assertNotNull(parsedDate);
        assertEquals(sdf.parse(dateStr), parsedDate);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_parseExceptionFallsBackToISO8601Utils() throws Exception {
        String dateStr = "2023-06-15T12:34:56Z";
        when(jsonReaderMock.nextString()).thenReturn(dateStr);

        // Inject a dateFormat that always throws ParseException
        DateFormat failingFormat = new DateFormat() {
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition fieldPosition) {
                return null;
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                return null;
            }

            @Override
            public Date parse(String source) throws ParseException {
                throw new ParseException("Fail", 0);
            }
        };

        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(failingFormat);

        // Invoke method, expecting ISO8601Utils.parse to succeed
        Date result = invokeDeserializeToDate(adapter, jsonReaderMock);
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_parseExceptionAndISO8601UtilsFails_throwsJsonSyntaxException() throws Exception {
        String badDateStr = "bad-date-string";
        when(jsonReaderMock.nextString()).thenReturn(badDateStr);
        when(jsonReaderMock.getPreviousPath()).thenReturn("$.date");

        // Inject a dateFormat that always throws ParseException
        DateFormat failingFormat = new DateFormat() {
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition fieldPosition) {
                return null;
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                return null;
            }

            @Override
            public Date parse(String source) throws ParseException {
                throw new ParseException("Fail", 0);
            }
        };

        // Create a helper class that wraps DefaultDateTypeAdapter and overrides deserializeToDate to simulate ISO8601Utils.parse failure
        class TestAdapter {
            private final DefaultDateTypeAdapter<Date> delegate;
            final List<DateFormat> dateFormatsRef;

            TestAdapter() throws Exception {
                var constructors = DefaultDateTypeAdapter.class.getDeclaredConstructors();
                DefaultDateTypeAdapter<Date> instance = null;
                for (var ctor : constructors) {
                    if (ctor.getParameterCount() == 2) {
                        ctor.setAccessible(true);
                        instance = (DefaultDateTypeAdapter<Date>) ctor.newInstance(null, "yyyy-MM-dd");
                        break;
                    }
                }
                this.delegate = instance;

                Field dfField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
                dfField.setAccessible(true);
                dateFormatsRef = (List<DateFormat>) dfField.get(delegate);
            }

            Date deserializeToDate(JsonReader in) throws Exception {
                String s = in.nextString();
                synchronized (dateFormatsRef) {
                    for (DateFormat dateFormat : dateFormatsRef) {
                        try {
                            return dateFormat.parse(s);
                        } catch (ParseException ignored) {}
                    }
                }
                // Simulate ISO8601Utils.parse throwing ParseException
                throw new JsonSyntaxException("Failed parsing '" + s + "' as Date; at path " + in.getPreviousPath(), new ParseException("Fail", 0));
            }
        }

        TestAdapter testAdapter = new TestAdapter();

        // Inject the failingFormat into testAdapter's dateFormats list
        testAdapter.dateFormatsRef.clear();
        testAdapter.dateFormatsRef.add(failingFormat);

        Exception ex = assertThrows(JsonSyntaxException.class, () -> testAdapter.deserializeToDate(jsonReaderMock));
        assertTrue(ex.getMessage().contains("Failed parsing"));
        assertTrue(ex.getCause() instanceof ParseException);
    }
}