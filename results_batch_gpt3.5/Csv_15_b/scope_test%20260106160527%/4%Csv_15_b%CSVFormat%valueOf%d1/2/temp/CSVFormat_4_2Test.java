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

class CSVFormat_4_2Test {

    @Test
    @Timeout(8000)
    void testValueOfKnownFormats() {
        // Test known predefined formats by name
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals("\r\n", format.getRecordSeparator());

        format = CSVFormat.valueOf("EXCEL");
        assertNotNull(format);
        assertFalse(format.getIgnoreEmptyLines());

        format = CSVFormat.valueOf("INFORMIX_UNLOAD");
        assertNotNull(format);
        assertEquals('|', format.getDelimiter());
        assertEquals("\n", format.getRecordSeparator());

        format = CSVFormat.valueOf("MYSQL");
        assertNotNull(format);
        assertEquals('\t', format.getDelimiter());
        assertEquals("\\N", format.getNullString());

        format = CSVFormat.valueOf("POSTGRESQL_CSV");
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
        assertEquals("", format.getNullString());

        format = CSVFormat.valueOf("POSTGRESQL_TEXT");
        assertNotNull(format);
        assertEquals('\t', format.getDelimiter());
        assertEquals("\\N", format.getNullString());

        format = CSVFormat.valueOf("RFC4180");
        assertNotNull(format);
        assertFalse(format.getIgnoreEmptyLines());

        format = CSVFormat.valueOf("TDF");
        assertNotNull(format);
        assertEquals('\t', format.getDelimiter());
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testValueOfCaseSensitivity() {
        // The predefined valueOf is case-sensitive, test that incorrect case throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("excel"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("MySQL"));
    }

    @Test
    @Timeout(8000)
    void testValueOfInvalidFormat() {
        // Test that an unknown format string throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN_FORMAT"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}