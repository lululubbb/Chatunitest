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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_29_3Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        char delimiter = ',';
        Character quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = '#';
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;

        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        Method withCommentMarkerMethod = CSVFormat.class.getDeclaredMethod("withCommentMarker", Character.class);
        withCommentMarkerMethod.setAccessible(true);
        CSVFormat newCsvFormat = (CSVFormat) withCommentMarkerMethod.invoke(csvFormat, commentMarker);

        // Then
        assertNotNull(newCsvFormat);
        assertEquals(commentMarker, newCsvFormat.getCommentMarker());
    }
}