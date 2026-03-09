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

class CSVFormat_74_2Test {

    @Test
    @Timeout(8000)
    void testWithTrimDefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withTrim(true);
        assertNotNull(result);
        assertTrue(result.getTrim());
        // Original instance should remain unchanged
        assertFalse(format.getTrim());
        // Chaining withTrim again returns new instance with trim true
        CSVFormat result2 = result.withTrim(true);
        assertTrue(result2.getTrim());
        // The new instance is different from the original
        assertNotSame(format, result);
        assertNotSame(result, result2);
    }

    @Test
    @Timeout(8000)
    void testWithTrimFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat result = format.withTrim(false);
        assertNotNull(result);
        assertFalse(result.getTrim());
        // Original instance remains unchanged
        assertTrue(format.getTrim());
        assertNotSame(format, result);
    }
}