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

class CSVFormat_41_3Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with common line break characters
        CSVFormat formatCR = baseFormat.withRecordSeparator('\r');
        assertEquals("\r", formatCR.getRecordSeparator());

        CSVFormat formatLF = baseFormat.withRecordSeparator('\n');
        assertEquals("\n", formatLF.getRecordSeparator());

        // Corrected: to test CRLF, use withRecordSeparator(String) instead of char
        CSVFormat formatCRLF = baseFormat.withRecordSeparator("\r\n");
        assertEquals("\r\n", formatCRLF.getRecordSeparator());

        // Test with a non-line break character
        CSVFormat formatX = baseFormat.withRecordSeparator('X');
        assertEquals("X", formatX.getRecordSeparator());

        // Test that original instance is not modified
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());

        // Test with special character tab
        CSVFormat formatTab = baseFormat.withRecordSeparator('\t');
        assertEquals("\t", formatTab.getRecordSeparator());
    }
}