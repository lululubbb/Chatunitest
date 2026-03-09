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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

public class CSVFormat_5_5Test {

    @Test
    @Timeout(8000)
    public void testEquals() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;

        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testNotEquals() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.EXCEL;

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithDifferentDelimiter() {
        CSVFormat format1 = CSVFormat.newFormat(',');
        CSVFormat format2 = CSVFormat.newFormat(';');

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithNullQuoteCharacter() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withQuote(null);

        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithDifferentHeader() {
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader("A", "B", "C");
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader("X", "Y", "Z");

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithIgnoreEmptyLines() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithIgnoreSurroundingSpaces() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithSkipHeaderRecord() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);

        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEqualsWithDifferentRecordSeparator() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT.withRecordSeparator("\r\n");

        assertFalse(format1.equals(format2));
    }
}