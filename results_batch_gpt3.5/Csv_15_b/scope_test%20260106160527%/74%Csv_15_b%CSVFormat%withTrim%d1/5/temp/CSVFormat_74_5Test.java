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

class CSVFormat_74_5Test {

    @Test
    @Timeout(8000)
    void testWithTrim() {
        // Using the default CSVFormat instance
        CSVFormat format = CSVFormat.DEFAULT;
        // Call withTrim() which delegates to withTrim(true)
        CSVFormat trimmedFormat = format.withTrim();

        // The returned instance should not be the same as the original if the trim flag changes
        if (format.getTrim()) {
            // If already trimmed, should return the same instance
            assertSame(format, trimmedFormat);
        } else {
            // Otherwise, should return a new instance with trim true
            assertNotSame(format, trimmedFormat);
            assertTrue(trimmedFormat.getTrim());
        }

        // Also test withTrim(false) returns a format with trim false
        CSVFormat untrimmedFormat = format.withTrim(false);
        assertFalse(untrimmedFormat.getTrim());

        // Test chaining does not affect original
        assertFalse(format.getTrim());

        // Test withTrim(true) on a format already trimmed returns same instance
        CSVFormat doubleTrimmed = trimmedFormat.withTrim(true);
        assertSame(trimmedFormat, doubleTrimmed);
    }
}