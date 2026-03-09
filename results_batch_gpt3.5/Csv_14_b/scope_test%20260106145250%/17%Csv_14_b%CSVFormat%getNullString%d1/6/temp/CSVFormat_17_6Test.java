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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_17_6Test {

    @Test
    @Timeout(8000)
    void testGetNullStringWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullStringWhenSet() throws Exception {
        // Since CSVFormat is immutable, withNullString returns a new instance.
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        assertEquals("NULL", format.getNullString());

        // Additionally, test by reflection to forcibly set nullString and verify getNullString
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        CSVFormat formatWithNullString = CSVFormat.DEFAULT;

        // Remove final modifier via reflection (if present)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(nullStringField, nullStringField.getModifiers() & ~Modifier.FINAL);

        nullStringField.set(formatWithNullString, "NULL_REFLECT");
        assertEquals("NULL_REFLECT", formatWithNullString.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullStringOnPredefinedFormats() {
        assertNull(CSVFormat.DEFAULT.getNullString());
        assertNull(CSVFormat.EXCEL.getNullString());
        assertNull(CSVFormat.INFORMIX_UNLOAD.getNullString());
        assertNull(CSVFormat.INFORMIX_UNLOAD_CSV.getNullString());
        assertEquals("\\N", CSVFormat.MYSQL.getNullString());
        assertNull(CSVFormat.RFC4180.getNullString());
        assertNull(CSVFormat.TDF.getNullString());
    }
}