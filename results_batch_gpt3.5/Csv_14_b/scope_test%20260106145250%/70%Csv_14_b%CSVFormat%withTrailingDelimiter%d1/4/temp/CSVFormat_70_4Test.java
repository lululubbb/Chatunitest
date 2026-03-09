package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CSVFormat_70_4Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_defaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withTrailingDelimiter(true);
        assertNotNull(newFormat);
        assertTrue(newFormat.getTrailingDelimiter());
        // Original format should remain unchanged
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_false() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat newFormat = format.withTrailingDelimiter(false);
        assertNotNull(newFormat);
        assertFalse(newFormat.getTrailingDelimiter());
        // The original format had trailingDelimiter true
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter_trueOnFormatAlreadyTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat newFormat = format.withTrailingDelimiter(true);
        assertNotNull(newFormat);
        assertTrue(newFormat.getTrailingDelimiter());
        // Should be the same instance, not a new one, if trailingDelimiter is unchanged
        assertSame(format, newFormat);
    }
}