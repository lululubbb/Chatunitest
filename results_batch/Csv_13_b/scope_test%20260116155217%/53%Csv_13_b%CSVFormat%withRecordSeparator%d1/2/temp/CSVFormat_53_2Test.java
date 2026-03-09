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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_53_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with normal character
        char separator = ';';
        CSVFormat formatWithSeparator = baseFormat.withRecordSeparator(separator);
        assertNotNull(formatWithSeparator);
        assertEquals(String.valueOf(separator), formatWithSeparator.getRecordSeparator());

        // Test with newline character '\n'
        separator = '\n';
        formatWithSeparator = baseFormat.withRecordSeparator(separator);
        assertNotNull(formatWithSeparator);
        assertEquals(String.valueOf(separator), formatWithSeparator.getRecordSeparator());

        // Test with carriage return '\r'
        separator = '\r';
        formatWithSeparator = baseFormat.withRecordSeparator(separator);
        assertNotNull(formatWithSeparator);
        assertEquals(String.valueOf(separator), formatWithSeparator.getRecordSeparator());

        // Test with tab character '\t'
        separator = '\t';
        formatWithSeparator = baseFormat.withRecordSeparator(separator);
        assertNotNull(formatWithSeparator);
        assertEquals(String.valueOf(separator), formatWithSeparator.getRecordSeparator());

        // Test with default record separator (CRLF)
        // Since withRecordSeparator(char) accepts only a single char,
        // test with '\n' which is part of CRLF
        separator = '\n';
        formatWithSeparator = baseFormat.withRecordSeparator(separator);
        assertNotNull(formatWithSeparator);
        assertEquals(String.valueOf(separator), formatWithSeparator.getRecordSeparator());
    }
}