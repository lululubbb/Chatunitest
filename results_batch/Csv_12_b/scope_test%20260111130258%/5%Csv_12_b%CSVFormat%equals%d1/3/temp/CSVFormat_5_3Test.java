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
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.util.Arrays;

public class CSVFormat_5_3Test {

    private CSVFormat csvFormat1;
    private CSVFormat csvFormat2;

    @BeforeEach
    public void setup() {
        csvFormat1 = CSVFormat.DEFAULT;
        csvFormat2 = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testEquals_SameInstance() {
        assertTrue(csvFormat1.equals(csvFormat1));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject() {
        assertFalse(csvFormat1.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass() {
        assertFalse(csvFormat1.equals("Not a CSVFormat object"));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EqualObjects() {
        assertTrue(csvFormat1.equals(csvFormat2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentDelimiter() {
        CSVFormat csvFormatDiffDelimiter = CSVFormat.DEFAULT.withDelimiter('|');
        assertFalse(csvFormat1.equals(csvFormatDiffDelimiter));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentQuoteCharacter() {
        CSVFormat csvFormatDiffQuote = CSVFormat.DEFAULT.withQuote('\'');
        assertFalse(csvFormat1.equals(csvFormatDiffQuote));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentCommentMarker() {
        CSVFormat csvFormatDiffComment = CSVFormat.DEFAULT.withCommentMarker('#');
        assertFalse(csvFormat1.equals(csvFormatDiffComment));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentEscapeCharacter() {
        CSVFormat csvFormatDiffEscape = CSVFormat.DEFAULT.withEscape('\\');
        assertFalse(csvFormat1.equals(csvFormatDiffEscape));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentNullString() {
        CSVFormat csvFormatDiffNullString = CSVFormat.DEFAULT.withNullString("NULL");
        assertFalse(csvFormat1.equals(csvFormatDiffNullString));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentHeader() {
        CSVFormat csvFormatDiffHeader = CSVFormat.DEFAULT.withHeader("Header1", "Header2");
        assertFalse(csvFormat1.equals(csvFormatDiffHeader));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreSurroundingSpaces() {
        CSVFormat csvFormatDiffSpaces = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertFalse(csvFormat1.equals(csvFormatDiffSpaces));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentIgnoreEmptyLines() {
        CSVFormat csvFormatDiffEmptyLines = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(csvFormat1.equals(csvFormatDiffEmptyLines));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentSkipHeaderRecord() {
        CSVFormat csvFormatDiffSkipHeader = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertFalse(csvFormat1.equals(csvFormatDiffSkipHeader));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentRecordSeparator() {
        CSVFormat csvFormatDiffRecordSeparator = CSVFormat.DEFAULT.withRecordSeparator("\n");
        assertFalse(csvFormat1.equals(csvFormatDiffRecordSeparator));
    }

}
