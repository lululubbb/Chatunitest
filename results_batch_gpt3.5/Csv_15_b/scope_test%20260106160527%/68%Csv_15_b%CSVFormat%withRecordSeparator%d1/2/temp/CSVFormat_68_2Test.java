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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_68_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char sep = '\n';
        CSVFormat newFormat = baseFormat.withRecordSeparator(sep);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(sep), newFormat.getRecordSeparator());
        // Ensure original instance is not modified (immutability)
        assertNotSame(baseFormat, newFormat);
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_differentSeparator() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char sep = '\r';
        CSVFormat newFormat = baseFormat.withRecordSeparator(sep);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(sep), newFormat.getRecordSeparator());
        assertNotSame(baseFormat, newFormat);
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_specialCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char sep = '\u2028'; // Unicode line separator
        CSVFormat newFormat = baseFormat.withRecordSeparator(sep);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(sep), newFormat.getRecordSeparator());
        assertNotSame(baseFormat, newFormat);
    }
}