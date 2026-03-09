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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_20_3Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() throws Exception {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '"', null, '#', '\\', true, false, "\r\n",
                null, null, false, true);

        // When
        boolean isCommentMarkerSet = invokeIsCommentMarkerSet(csvFormat);

        // Then
        assertTrue(isCommentMarkerSet);

        // Additional Test for when commentMarker is null
        csvFormat = createCSVFormat(',', '"', null, null, '\\', true, false, "\r\n",
                null, null, false, true);

        isCommentMarkerSet = invokeIsCommentMarkerSet(csvFormat);

        assertFalse(isCommentMarkerSet);
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
                                       Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                       String recordSeparator, String nullString, String[] header,
                                       boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        return CSVFormat.create(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
    }

    private boolean invokeIsCommentMarkerSet(CSVFormat csvFormat) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVFormat.class.getDeclaredMethod("isCommentMarkerSet");
        method.setAccessible(true);
        return (boolean) method.invoke(csvFormat);
    }
}