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

public class CSVFormat_74_6Test {

    @Test
    @Timeout(8000)
    public void testWithTrimDefault() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrim();
        assertNotNull(result);
        assertTrue(result.getTrim());
        // Ensure original is unchanged (immutable pattern)
        assertFalse(original.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testWithTrimTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrim(true);
        assertNotNull(result);
        assertTrue(result.getTrim());
        assertFalse(original.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testWithTrimFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat result = original.withTrim(false);
        assertNotNull(result);
        assertFalse(result.getTrim());
        assertTrue(original.getTrim());
    }
}