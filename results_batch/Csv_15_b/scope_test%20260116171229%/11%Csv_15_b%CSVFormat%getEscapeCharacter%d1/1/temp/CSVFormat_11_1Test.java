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

class CSVFormat_11_1Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "Default format should have null escapeCharacter");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_InfromixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "INFORMIX_UNLOAD should have escapeCharacter set");
        assertEquals('\\', escape.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "MYSQL should have escapeCharacter set");
        assertEquals('\\', escape.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgreSQLCSV() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "POSTGRESQL_CSV should have escapeCharacter set");
        assertEquals('"', escape.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgreSQLText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "POSTGRESQL_TEXT should have escapeCharacter set");
        assertEquals('"', escape.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_TDF() {
        CSVFormat format = CSVFormat.TDF;
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "TDF should have null escapeCharacter");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_CustomInstance() throws Exception {
        Class<?> quoteModeClass = Class.forName("org.apache.commons.csv.CSVFormat$QuoteMode");

        java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteModeClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
                ',', '"', null, null, '\\',
                false, false, "\n", null, null, null,
                false, false, false, false, false, false);
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals('\\', escape.charValue());
    }
}