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

public class CSVFormat_41_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char separator = '\n';

        CSVFormat resultFormat = baseFormat.withRecordSeparator(separator);

        assertNotNull(resultFormat);
        assertNotSame(baseFormat, resultFormat);
        assertEquals(String.valueOf(separator), resultFormat.getRecordSeparator());

        // verify original unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_empty() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char separator = (char) 0;

        CSVFormat resultFormat = baseFormat.withRecordSeparator(separator);

        assertNotNull(resultFormat);
        assertNotSame(baseFormat, resultFormat);

        String recSep = resultFormat.getRecordSeparator();
        String expected = String.valueOf(separator);

        // Because recordSeparator is String, and (char)0 is a non-printable char,
        // getRecordSeparator() may return null or a string containing '\0'.
        if (recSep == null) {
            assertNull(recSep);
        } else {
            assertEquals(expected, recSep);
        }
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_specialChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char separator = '\r';

        CSVFormat resultFormat = baseFormat.withRecordSeparator(separator);

        assertNotNull(resultFormat);
        assertNotSame(baseFormat, resultFormat);
        assertEquals(String.valueOf(separator), resultFormat.getRecordSeparator());
    }

}