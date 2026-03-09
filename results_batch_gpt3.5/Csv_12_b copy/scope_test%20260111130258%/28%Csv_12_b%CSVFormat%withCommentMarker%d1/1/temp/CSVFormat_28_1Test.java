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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_28_1Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertEquals(Character.valueOf(commentMarker), newCsvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Character() {
        // Given
        Character commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertEquals(commentMarker, newCsvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getDeclaredMethod("isCommentMarkerSet");
        method.setAccessible(true);

        // When
        boolean result = (boolean) method.invoke(csvFormat);

        // Then
        assertFalse(result);
    }

    // Add more test cases for other methods if needed
}