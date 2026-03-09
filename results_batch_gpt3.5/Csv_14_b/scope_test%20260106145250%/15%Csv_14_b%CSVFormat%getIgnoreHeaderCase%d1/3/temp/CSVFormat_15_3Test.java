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

import java.lang.reflect.Field;

class CSVFormat_15_3Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_DefaultInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);
        boolean ignoreHeaderCaseValue = field.getBoolean(format);
        assertFalse(ignoreHeaderCaseValue);
        assertEquals(ignoreHeaderCaseValue, format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);
        boolean ignoreHeaderCaseValue = field.getBoolean(format);
        assertTrue(ignoreHeaderCaseValue);
        assertEquals(ignoreHeaderCaseValue, format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);
        boolean ignoreHeaderCaseValue = field.getBoolean(format);
        assertFalse(ignoreHeaderCaseValue);
        assertEquals(ignoreHeaderCaseValue, format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_OtherPredefinedConstants() throws Exception {
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);

        assertFalse(field.getBoolean(CSVFormat.EXCEL));
        assertFalse(CSVFormat.EXCEL.getIgnoreHeaderCase());

        assertFalse(field.getBoolean(CSVFormat.INFORMIX_UNLOAD));
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getIgnoreHeaderCase());

        assertFalse(field.getBoolean(CSVFormat.INFORMIX_UNLOAD_CSV));
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getIgnoreHeaderCase());

        assertFalse(field.getBoolean(CSVFormat.MYSQL));
        assertFalse(CSVFormat.MYSQL.getIgnoreHeaderCase());

        assertFalse(field.getBoolean(CSVFormat.RFC4180));
        assertFalse(CSVFormat.RFC4180.getIgnoreHeaderCase());

        assertFalse(field.getBoolean(CSVFormat.TDF));
        assertFalse(CSVFormat.TDF.getIgnoreHeaderCase());
    }
}