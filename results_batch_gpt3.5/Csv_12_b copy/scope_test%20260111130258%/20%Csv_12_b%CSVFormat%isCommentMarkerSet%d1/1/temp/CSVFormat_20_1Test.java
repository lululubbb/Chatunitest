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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class CSVFormat_20_1Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withCommentMarker('#');

        // When
        boolean isCommentMarkerSet = csvFormat.isCommentMarkerSet();

        // Then
        assertTrue(isCommentMarkerSet);
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_NullCommentMarker() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withCommentMarker(null);

        // When
        boolean isCommentMarkerSet = csvFormat.isCommentMarkerSet();

        // Then
        assertFalse(isCommentMarkerSet);
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_PrivateCommentMarker() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withCommentMarker(null);
        CSVFormat spyCsvFormat = Mockito.spy(csvFormat);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        commentMarkerField.set(spyCsvFormat, '#');

        // When
        boolean isCommentMarkerSet = spyCsvFormat.isCommentMarkerSet();

        // Then
        assertTrue(isCommentMarkerSet);
    }

}