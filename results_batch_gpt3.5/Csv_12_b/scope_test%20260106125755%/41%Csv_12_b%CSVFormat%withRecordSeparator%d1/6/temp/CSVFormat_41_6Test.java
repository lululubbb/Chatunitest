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

class CSVFormat_41_6Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char newSeparator = '\n';

        CSVFormat newFormat = baseFormat.withRecordSeparator(newSeparator);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertEquals(String.valueOf(newSeparator), newFormat.getRecordSeparator());

        // Original format unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_various() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with CR
        CSVFormat crFormat = baseFormat.withRecordSeparator('\r');
        assertEquals("\r", crFormat.getRecordSeparator());

        // Test with LF
        CSVFormat lfFormat = baseFormat.withRecordSeparator('\n');
        assertEquals("\n", lfFormat.getRecordSeparator());

        // Test with tab character
        CSVFormat tabFormat = baseFormat.withRecordSeparator('\t');
        assertEquals("\t", tabFormat.getRecordSeparator());

        // Test with comma character
        CSVFormat commaFormat = baseFormat.withRecordSeparator(',');
        assertEquals(",", commaFormat.getRecordSeparator());
    }
}