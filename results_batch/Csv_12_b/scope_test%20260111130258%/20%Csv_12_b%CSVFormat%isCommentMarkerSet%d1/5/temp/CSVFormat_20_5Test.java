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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_20_5Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withCommentMarker('#')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\r\n")
                .withHeader((String[]) null)
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true);

        // When
        boolean result = invokePrivateMethod(csvFormat, "isCommentMarkerSet");

        // Then
        assertTrue(result);
    }

    private boolean invokePrivateMethod(CSVFormat csvFormat, String methodName) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return (boolean) method.invoke(csvFormat);
    }
}