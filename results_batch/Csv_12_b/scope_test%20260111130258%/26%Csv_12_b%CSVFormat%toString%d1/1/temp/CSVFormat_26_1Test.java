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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_26_1Test {

    @Test
    @Timeout(8000)
    public void testToString() {
        // Given
        char delimiter = ',';
        char escapeCharacter = '\\';
        char quoteCharacter = '"';
        char commentMarker = '#';
        String recordSeparator = "\r\n";
        String nullString = "NULL";
        String[] header = {"Header1", "Header2"};
        boolean skipHeaderRecord = true;

        // Creating CSVFormat instance using reflection as the constructor is private
        CSVFormat csvFormat = null;
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                    Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            csvFormat = constructor.newInstance(delimiter, quoteCharacter, null, commentMarker, escapeCharacter, false, true, recordSeparator, nullString, header, skipHeaderRecord, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // When
        String result = csvFormat.toString();

        // Then
        String expected = "Delimiter=<,> Escape=<\\> QuoteChar<\"> CommentStart=<#> RecordSeparator=<\r\n> SkipHeaderRecord:true Header:[Header1, Header2]";
        assertEquals(expected, result);
    }
}