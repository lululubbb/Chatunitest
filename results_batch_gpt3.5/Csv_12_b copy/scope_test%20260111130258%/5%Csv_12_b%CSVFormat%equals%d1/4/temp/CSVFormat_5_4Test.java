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

public class CSVFormat_5_4Test {

    @Test
    @Timeout(8000)
    public void testEquals() {
        // Given
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = CSVFormat.DEFAULT;
        
        // When/Then
        assertTrue(format1.equals(format2));

        // Testing different delimiter
        CSVFormat format3 = CSVFormat.DEFAULT.withDelimiter(';');
        assertFalse(format1.equals(format3));

        // Testing different quote mode
        CSVFormat format4 = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        assertFalse(format1.equals(format4));

        // Testing different quote character
        CSVFormat format5 = CSVFormat.DEFAULT.withQuote('"');
        assertFalse(format1.equals(format5));

        // Testing different comment marker
        CSVFormat format6 = CSVFormat.DEFAULT.withCommentMarker('#');
        assertFalse(format1.equals(format6));

        // Testing different escape character
        CSVFormat format7 = CSVFormat.DEFAULT.withEscape('\\');
        assertFalse(format1.equals(format7));

        // Testing different null string
        CSVFormat format8 = CSVFormat.DEFAULT.withNullString("NULL");
        assertFalse(format1.equals(format8));

        // Testing different header
        CSVFormat format9 = CSVFormat.DEFAULT.withHeader("A", "B", "C");
        assertFalse(format1.equals(format9));

        // Testing different ignore surrounding spaces
        CSVFormat format10 = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertFalse(format1.equals(format10));

        // Testing different ignore empty lines
        CSVFormat format11 = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format1.equals(format11));

        // Testing different skip header record
        CSVFormat format12 = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertFalse(format1.equals(format12));

        // Testing different record separator
        CSVFormat format13 = CSVFormat.DEFAULT.withRecordSeparator("\r\n");
        assertFalse(format1.equals(format13));
    }

    // Add more test cases as needed for optimal coverage

}