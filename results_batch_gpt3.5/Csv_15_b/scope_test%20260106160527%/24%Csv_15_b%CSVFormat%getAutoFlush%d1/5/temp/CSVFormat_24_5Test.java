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

class CSVFormat_24_5Test {

    @Test
    @Timeout(8000)
    void testGetAutoFlush_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_CustomInstanceTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAutoFlush(true);
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_CustomInstanceFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAutoFlush(false);
        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_ReflectionAccess() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAutoFlush(true);
        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(autoFlushField, autoFlushField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        autoFlushField.set(format, false);
        boolean autoFlushValue = format.getAutoFlush();
        assertFalse(autoFlushValue);
    }
}