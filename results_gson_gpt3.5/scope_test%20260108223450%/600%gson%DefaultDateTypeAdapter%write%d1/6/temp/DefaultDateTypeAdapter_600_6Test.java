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
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class DefaultDateTypeAdapter_600_6Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonWriter jsonWriterMock;
    private DateFormat dateFormatMock;
    private List<DateFormat> dateFormatsList;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a DateType anonymous implementation passing Class<Date> to constructor
        DefaultDateTypeAdapter.DateType<Date> dateType = new DefaultDateTypeAdapter.DateType<Date>(Date.class) {
            @Override
            public Date deserialize(Date s) {
                return null;
            }
        };

        // Instantiate adapter with reflection because constructors are private
        var constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, String.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Mock JsonWriter
        jsonWriterMock = mock(JsonWriter.class);

        // Prepare a mock DateFormat and inject it into adapter's dateFormats list
        dateFormatMock = mock(DateFormat.class);
        dateFormatsList = (List<DateFormat>) getField(adapter, "dateFormats");
        dateFormatsList.clear();
        dateFormatsList.add(dateFormatMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullValue_callsNullValue() throws IOException {
        adapter.write(jsonWriterMock, null);
        verify(jsonWriterMock).nullValue();
        verifyNoMoreInteractions(jsonWriterMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_nonNullValue_formatsAndWrites() throws IOException {
        Date date = new Date();
        String formattedDate = "formatted-date-string";

        when(dateFormatMock.format(date)).thenReturn(formattedDate);

        adapter.write(jsonWriterMock, date);

        InOrder inOrder = inOrder(dateFormatMock, jsonWriterMock);
        // Verify synchronized block calls format on dateFormatMock
        inOrder.verify(dateFormatMock).format(date);
        // Verify out.value is called with formatted string
        inOrder.verify(jsonWriterMock).value(formattedDate);
        verifyNoMoreInteractions(dateFormatMock, jsonWriterMock);
    }

    private Object getField(Object instance, String fieldName) throws Exception {
        var field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}