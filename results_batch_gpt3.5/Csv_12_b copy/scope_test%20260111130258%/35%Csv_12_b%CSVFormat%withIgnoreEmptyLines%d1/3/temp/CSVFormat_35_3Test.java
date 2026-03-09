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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

public class CSVFormat_35_3Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, false, false);
        boolean newIgnoreEmptyLines = false;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(newIgnoreEmptyLines);

        // Then
        assertEquals(newIgnoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_RFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;
        boolean newIgnoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(newIgnoreEmptyLines);

        // Then
        assertEquals(newIgnoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_Excel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;
        boolean newIgnoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(newIgnoreEmptyLines);

        // Then
        assertEquals(newIgnoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_TDF() {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;
        boolean newIgnoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(newIgnoreEmptyLines);

        // Then
        assertEquals(newIgnoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_MYSQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;
        boolean newIgnoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(newIgnoreEmptyLines);

        // Then
        assertEquals(newIgnoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteChar, QuoteMode quoteMode, Character commentStart,
                                      Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header,
                                      boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                    QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                    String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                    skipHeaderRecord, allowMissingColumnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}