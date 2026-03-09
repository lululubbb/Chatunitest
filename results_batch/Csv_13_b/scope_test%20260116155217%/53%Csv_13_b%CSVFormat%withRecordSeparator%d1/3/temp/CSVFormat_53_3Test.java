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
import org.junit.jupiter.api.BeforeEach;

class CSVFormat_53_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        // Test with normal char
        char separator = '\n';
        CSVFormat newFormat = csvFormat.withRecordSeparator(separator);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());
        // Original instance unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), csvFormat.getRecordSeparator());

        // Test with carriage return char
        separator = '\r';
        newFormat = csvFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());

        // Test with tab char as separator (unusual but possible)
        separator = '\t';
        newFormat = csvFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());

        // Test with a non-printable char
        separator = 0x1F;
        newFormat = csvFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());
    }
}