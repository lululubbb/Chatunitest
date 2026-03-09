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

public class CSVFormat_74_1Test {

    @Test
    @Timeout(8000)
    public void testWithTrim() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat trimmedFormat = format.withTrim();

        assertNotNull(trimmedFormat);
        assertTrue(trimmedFormat.getTrim());
        // Original format should remain unchanged (immutable)
        assertFalse(format.getTrim());

        // Also test withTrim(false) returns a format with trim == false
        CSVFormat untrimmedFormat = format.withTrim(false);
        assertNotNull(untrimmedFormat);
        assertFalse(untrimmedFormat.getTrim());

        // Test chaining does not affect original
        CSVFormat chained = format.withTrim().withIgnoreEmptyLines(false);
        assertTrue(chained.getTrim());
        assertFalse(format.getTrim());
    }
}