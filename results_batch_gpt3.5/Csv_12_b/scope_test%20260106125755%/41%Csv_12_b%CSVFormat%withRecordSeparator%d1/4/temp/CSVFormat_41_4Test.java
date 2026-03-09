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

class CSVFormat_41_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char separator = '\n';
        CSVFormat updated = original.withRecordSeparator(separator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(separator), updated.getRecordSeparator());

        // Original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());

        // Test with different char
        updated = original.withRecordSeparator('\r');
        assertEquals("\r", updated.getRecordSeparator());

        // Test with special char
        updated = original.withRecordSeparator('\u2028'); // Unicode line separator
        assertEquals("\u2028", updated.getRecordSeparator());
    }
}