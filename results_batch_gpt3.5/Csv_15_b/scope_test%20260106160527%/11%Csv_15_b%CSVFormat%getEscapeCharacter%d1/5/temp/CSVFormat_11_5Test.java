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

class CSVFormat_11_5Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "Default format escape character should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "Escape character should not be null");
        assertEquals(Character.valueOf('\\'), escape, "Escape character should be backslash");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_WithNullEscape() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "Escape character explicitly set to null should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('\\'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('\\'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgreSQLCSV() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('"'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgreSQLText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('"'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_TDF() {
        CSVFormat format = CSVFormat.TDF;
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "TDF format escape character should be null");
    }
}