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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_72_2Test {

    @Test
    @Timeout(8000)
    void testWithTrim_defaultTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withTrim();

        assertNotNull(result);
        assertTrue(result.getTrim());
        // The original should remain unchanged (immutability)
        assertFalse(original.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrim_explicitFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat result = original.withTrim(false);

        assertNotNull(result);
        assertFalse(result.getTrim());
        // The original should remain unchanged
        assertTrue(original.getTrim());
    }

    @Test
    @Timeout(8000)
    void testWithTrim_explicitTrue() {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(false);
        CSVFormat result = original.withTrim(true);

        assertNotNull(result);
        assertTrue(result.getTrim());
        // The original should remain unchanged
        assertFalse(original.getTrim());
    }
}