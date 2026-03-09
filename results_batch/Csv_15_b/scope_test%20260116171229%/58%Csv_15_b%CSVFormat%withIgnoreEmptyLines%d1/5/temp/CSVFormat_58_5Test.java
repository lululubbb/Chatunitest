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

public class CSVFormat_58_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_defaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreEmptyLines();
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreEmptyLines());
        // The original should remain unchanged
        assertTrue(format.getIgnoreEmptyLines());
        // The returned instance should be a different object if state changed
        if (format.getIgnoreEmptyLines()) {
            assertSame(format, newFormat);
        } else {
            assertNotSame(format, newFormat);
        }
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_false() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertNotNull(format);
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines_true() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        CSVFormat newFormat = format.withIgnoreEmptyLines(true);
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreEmptyLines());
        assertFalse(format.getIgnoreEmptyLines());
        assertNotSame(format, newFormat);
    }
}