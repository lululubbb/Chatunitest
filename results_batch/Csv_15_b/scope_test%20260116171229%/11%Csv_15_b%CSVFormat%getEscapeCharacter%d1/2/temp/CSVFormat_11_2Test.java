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

class CSVFormat_11_2Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = format.getEscapeCharacter();
        assertNull(escapeChar, "Default format escape character should be null");
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "Informix unload escape character should not be null");
        assertEquals(Character.valueOf('\\'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_Mysql() {
        CSVFormat format = CSVFormat.MYSQL;
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "MySQL escape character should not be null");
        assertEquals(Character.valueOf('\\'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgresqlCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "PostgreSQL CSV escape character should not be null");
        assertEquals(Character.valueOf('"'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_PostgresqlText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        Character escapeChar = format.getEscapeCharacter();
        assertNotNull(escapeChar, "PostgreSQL Text escape character should not be null");
        assertEquals(Character.valueOf('"'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_CustomWithEscape() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat custom = base.withEscape('X');
        Character escapeChar = custom.getEscapeCharacter();
        assertNotNull(escapeChar);
        assertEquals(Character.valueOf('X'), escapeChar);
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacter_CustomWithEscapeNull() {
        CSVFormat base = CSVFormat.DEFAULT.withEscape('X');
        CSVFormat custom = base.withEscape((Character) null);
        Character escapeChar = custom.getEscapeCharacter();
        assertNull(escapeChar);
    }
}