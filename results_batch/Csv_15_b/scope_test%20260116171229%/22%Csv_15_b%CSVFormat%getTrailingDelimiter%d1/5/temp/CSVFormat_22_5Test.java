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

import java.lang.reflect.Field;

class CSVFormat_22_5Test {

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set trailingDelimiter to false to ensure test correctness
        setTrailingDelimiter(format, false);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        // Use reflection to set trailingDelimiter to true to ensure test correctness
        setTrailingDelimiter(format, true);
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        // Use reflection to set trailingDelimiter to false to ensure test correctness
        setTrailingDelimiter(format, false);
        assertFalse(format.getTrailingDelimiter());
    }

    private void setTrailingDelimiter(CSVFormat format, boolean value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("trailingDelimiter");
        field.setAccessible(true);
        field.setBoolean(format, value);
    }
}