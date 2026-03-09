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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_27_5Test {

    @Test
    @Timeout(8000)
    void testValidate() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");

        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                validateMethod.invoke(csvFormat);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        CSVFormat csvFormat2 = CSVFormat.newFormat(',')
                .withQuote('"')
                .withEscape('\\')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                validateMethod.invoke(csvFormat2);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        CSVFormat csvFormat3 = CSVFormat.newFormat(',')
                .withQuote('"')
                .withCommentMarker('#')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                validateMethod.invoke(csvFormat3);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        CSVFormat csvFormat4 = CSVFormat.newFormat(',')
                .withQuote('#')
                .withCommentMarker('#')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                validateMethod.invoke(csvFormat4);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        CSVFormat csvFormat5 = CSVFormat.newFormat(',')
                .withQuote(null)
                .withQuoteMode(QuoteMode.NONE)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withRecordSeparator("\r\n");
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                validateMethod.invoke(csvFormat5);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}