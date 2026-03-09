package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Objects;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_600_5Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonWriter jsonWriterMock;
    private DateFormat dateFormatMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock DateType<Date> as it is required for constructor
        DateType<Date> dateTypeMock = mock(DateType.class);

        // Use reflection to invoke the private constructor
        var constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DateType.class, String.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateTypeMock, "yyyy-MM-dd");

        // Mock JsonWriter
        jsonWriterMock = mock(JsonWriter.class);

        // Mock DateFormat and inject it into adapter.dateFormats list
        dateFormatMock = mock(DateFormat.class);

        // Use reflection to get the dateFormats field and replace its content with our mock
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        List<DateFormat> dateFormatsList = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormatsList.clear();
        dateFormatsList.add(dateFormatMock);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriterMock, null);
        verify(jsonWriterMock).nullValue();
        verifyNoMoreInteractions(jsonWriterMock);
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_formatsDateAndWritesValue() throws IOException {
        Date testDate = new Date();
        String formattedDate = "2024-06-01";

        // Setup mock behavior for dateFormat.format
        when(dateFormatMock.format(testDate)).thenReturn(formattedDate);

        adapter.write(jsonWriterMock, testDate);

        InOrder inOrder = inOrder(dateFormatMock, jsonWriterMock);
        inOrder.verify(dateFormatMock).format(testDate);
        inOrder.verify(jsonWriterMock).value(formattedDate);
        verifyNoMoreInteractions(dateFormatMock, jsonWriterMock);
    }
}