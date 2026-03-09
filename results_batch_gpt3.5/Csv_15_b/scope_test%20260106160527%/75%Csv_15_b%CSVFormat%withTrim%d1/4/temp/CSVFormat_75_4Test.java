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

public class CSVFormat_75_4Test {

    @Test
    @Timeout(8000)
    public void testWithTrimTrue() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withTrim(true);
        assertNotNull(modified);
        assertNotSame(original, modified);

        // Using reflection to check the private final field 'trim'
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trimValue = (boolean) trimField.get(modified);
        assertTrue(trimValue);

        // Other fields should remain the same
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char originalDelimiter = (char) delimiterField.get(original);
        char modifiedDelimiter = (char) delimiterField.get(modified);
        assertEquals(originalDelimiter, modifiedDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testWithTrimFalse() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(true);
        CSVFormat modified = original.withTrim(false);
        assertNotNull(modified);
        assertNotSame(original, modified);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trimValue = (boolean) trimField.get(modified);
        assertFalse(trimValue);

        // Other fields should remain the same
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char originalDelimiter = (char) delimiterField.get(original);
        char modifiedDelimiter = (char) delimiterField.get(modified);
        assertEquals(originalDelimiter, modifiedDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testWithTrimIdempotent() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT.withTrim(false);
        CSVFormat modified = original.withTrim(false);
        assertNotNull(modified);
        // The original and modified should be the same instance when no change occurs
        assertSame(original, modified);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trimValue = (boolean) trimField.get(modified);
        assertFalse(trimValue);
    }
}