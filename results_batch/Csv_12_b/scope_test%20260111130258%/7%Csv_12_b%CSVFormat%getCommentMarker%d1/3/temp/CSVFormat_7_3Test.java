package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_7_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker() throws Exception {
        // Given
        char expectedCommentMarker = '#';
        CSVFormat csvFormat = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class).newInstance(',', '\"', null, expectedCommentMarker, null,
                false, true, "\r\n", null, null, false, false);

        // When
        Character actualCommentMarker = csvFormat.getCommentMarker();

        // Then
        assertEquals(expectedCommentMarker, actualCommentMarker);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarkerWhenNotSet() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class).newInstance(',', '\"', null, null, null,
                false, true, "\r\n", null, null, false, false);

        // When
        Character actualCommentMarker = csvFormat.getCommentMarker();

        // Then
        assertEquals(null, actualCommentMarker);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarkerUsingMockito() throws Exception {
        // Given
        char expectedCommentMarker = '#';
        CSVFormat csvFormatMock = mock(CSVFormat.class);
        when(csvFormatMock.getCommentMarker()).thenReturn(expectedCommentMarker);

        // When
        Character actualCommentMarker = csvFormatMock.getCommentMarker();

        // Then
        assertEquals(expectedCommentMarker, actualCommentMarker);
    }
}