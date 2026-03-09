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

class CSVFormat_22_4Test {

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_DefaultInstance() {
        // DEFAULT has trailingDelimiter = false
        assertFalse(CSVFormat.DEFAULT.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_ConstantFormats() throws Exception {
        // Use reflection to check trailingDelimiter for constant formats
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.EXCEL));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.INFORMIX_UNLOAD));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.INFORMIX_UNLOAD_CSV));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.MYSQL));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.POSTGRESQL_CSV));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.POSTGRESQL_TEXT));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.RFC4180));
        assertFalse(getTrailingDelimiterViaReflection(CSVFormat.TDF));
    }

    private boolean getTrailingDelimiterViaReflection(CSVFormat format) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("trailingDelimiter");
        field.setAccessible(true);
        return field.getBoolean(format);
    }
}