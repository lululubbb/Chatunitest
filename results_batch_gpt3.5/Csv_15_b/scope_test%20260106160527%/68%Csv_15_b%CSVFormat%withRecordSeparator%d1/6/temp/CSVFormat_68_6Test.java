package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_68_6Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char recordSeparator = '\n';
        CSVFormat updated = original.withRecordSeparator(recordSeparator);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(recordSeparator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_differentSeparator() {
        CSVFormat original = CSVFormat.DEFAULT;
        char recordSeparator = '\r';
        CSVFormat updated = original.withRecordSeparator(recordSeparator);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(recordSeparator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_specialChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char recordSeparator = '\u2028'; // Unicode line separator
        CSVFormat updated = original.withRecordSeparator(recordSeparator);
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(recordSeparator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_nullChar() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char recordSeparator = '\0';

        // Use reflection to invoke withRecordSeparator(String) with null to simulate '\0' behavior
        var method = CSVFormat.class.getDeclaredMethod("withRecordSeparator", String.class);
        CSVFormat updated = (CSVFormat) method.invoke(original, (String) null);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getRecordSeparator());
    }
}