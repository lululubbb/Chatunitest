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

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_27_4Test {

    @Test
    @Timeout(8000)
    public void testValidate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);

        assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat);
        });

        CSVFormat csvFormat2 = CSVFormat.newFormat(',')
                .withQuote(',')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat2);
        });

        CSVFormat csvFormat3 = CSVFormat.newFormat(',')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat3);
        });

        CSVFormat csvFormat4 = CSVFormat.newFormat(',')
                .withQuote('"')
                .withEscape('"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat4);
        });

        CSVFormat csvFormat5 = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat5);
        });

        CSVFormat csvFormat6 = CSVFormat.newFormat(',')
                .withQuote('"')
                .withQuoteMode(QuoteMode.NONE)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n");

        assertThrows(IllegalArgumentException.class, () -> {
            validateMethod.invoke(csvFormat6);
        });
    }
}