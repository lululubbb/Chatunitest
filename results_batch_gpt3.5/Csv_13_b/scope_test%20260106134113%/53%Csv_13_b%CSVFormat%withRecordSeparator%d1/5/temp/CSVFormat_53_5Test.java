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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_53_5Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char sep = '\n';
        CSVFormat updated = original.withRecordSeparator(sep);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(sep), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorCharDifferent() {
        CSVFormat original = CSVFormat.DEFAULT;
        char sep = '\r';
        CSVFormat updated = original.withRecordSeparator(sep);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(sep), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorCharSameValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        String originalSep = original.getRecordSeparator();
        char sep = (originalSep != null && originalSep.length() == 1) ? originalSep.charAt(0) : '\r';
        CSVFormat updated1 = original.withRecordSeparator(sep);
        CSVFormat updated2 = updated1.withRecordSeparator(sep);
        assertEquals(updated1.getRecordSeparator(), updated2.getRecordSeparator());
        assertSame(updated1, updated2);
    }
}