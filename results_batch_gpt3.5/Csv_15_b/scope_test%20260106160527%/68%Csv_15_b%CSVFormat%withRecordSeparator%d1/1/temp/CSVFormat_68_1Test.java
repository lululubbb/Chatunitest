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

class CSVFormat_68_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\n';

        CSVFormat updated = original.withRecordSeparator(newSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(newSeparator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_different() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\r';

        CSVFormat updated = original.withRecordSeparator(newSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(newSeparator), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_sameValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        // The default record separator is "\r\n", so test with '\r' to ensure difference
        char existingSeparator = '\r';

        CSVFormat updated = original.withRecordSeparator(existingSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(existingSeparator), updated.getRecordSeparator());
    }
}