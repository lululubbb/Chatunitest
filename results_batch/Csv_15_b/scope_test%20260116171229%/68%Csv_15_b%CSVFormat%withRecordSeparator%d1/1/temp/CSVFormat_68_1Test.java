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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_68_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char separator = '\n';
        CSVFormat updated = original.withRecordSeparator(separator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(separator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorCharDifferent() {
        CSVFormat original = CSVFormat.DEFAULT;
        char separator = '\r';
        CSVFormat updated = original.withRecordSeparator(separator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(separator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorCharSpecial() {
        CSVFormat original = CSVFormat.DEFAULT;
        char separator = '\u2028'; // Unicode line separator
        CSVFormat updated = original.withRecordSeparator(separator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(separator), updated.getRecordSeparator());
    }
}