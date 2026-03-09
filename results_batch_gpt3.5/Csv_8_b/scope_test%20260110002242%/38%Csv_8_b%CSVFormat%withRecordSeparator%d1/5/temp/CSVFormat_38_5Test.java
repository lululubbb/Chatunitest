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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_38_5Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\n';

        CSVFormat updated = original.withRecordSeparator(newSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(newSeparator), updated.getRecordSeparator());
        // Original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());
    }
}