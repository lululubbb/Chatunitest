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

public class CSVFormat_20_2Test {

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
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true);

        // When
        boolean result = csvFormat.isCommentMarkerSet();

        // Then
        assertTrue(result);

        // Using reflection to test with comment marker as null
        CSVFormat csvFormatNullCommentMarker = CSVFormat.newFormat(',')
                .withQuote('"')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true);
        assertFalse((boolean) invokePrivateMethod(csvFormatNullCommentMarker, "isCommentMarkerSet"));
    }

    private Object invokePrivateMethod(Object object, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object);
    }
}