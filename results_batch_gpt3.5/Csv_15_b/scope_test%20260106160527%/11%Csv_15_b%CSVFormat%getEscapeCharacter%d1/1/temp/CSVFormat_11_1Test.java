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
    void testGetEscapeCharacter_DefaultInstance() {
        // DEFAULT instance has escapeCharacter = null
        assertNull(CSVFormat.DEFAULT.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_InformixUnload() {
        // INFORMIX_UNLOAD instance has escapeCharacter = BACKSLASH
        Character escape = CSVFormat.INFORMIX_UNLOAD.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('\\'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_MySQL() {
        // MYSQL instance has escapeCharacter = BACKSLASH
        Character escape = CSVFormat.MYSQL.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('\\'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgreSQLCSV() {
        // POSTGRESQL_CSV instance has escapeCharacter = DOUBLE_QUOTE_CHAR
        Character escape = CSVFormat.POSTGRESQL_CSV.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('"'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgreSQLText() {
        // POSTGRESQL_TEXT instance has escapeCharacter = DOUBLE_QUOTE_CHAR
        Character escape = CSVFormat.POSTGRESQL_TEXT.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals(Character.valueOf('"'), escape);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_RFC4180() {
        // RFC4180 inherits from DEFAULT, escapeCharacter == null
        assertNull(CSVFormat.RFC4180.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_TDF() {
        // TDF inherits from DEFAULT, escapeCharacter == null
        assertNull(CSVFormat.TDF.getEscapeCharacter());
    }
}