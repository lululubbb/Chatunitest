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

public class CSVFormat_11_2Test {

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character escape = format.getEscapeCharacter();
        assertNull(escape, "Default CSVFormat escapeCharacter should be null");
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "INFORMIX_UNLOAD escapeCharacter should not be null");
        assertEquals('\\', escape.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "MYSQL escapeCharacter should not be null");
        assertEquals('\\', escape.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_PostgreSQLCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "POSTGRESQL_CSV escapeCharacter should not be null");
        assertEquals('"', escape.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_PostgreSQLText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape, "POSTGRESQL_TEXT escapeCharacter should not be null");
        assertEquals('"', escape.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_ExplicitWithEscapeChar() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat format = base.withEscape('\\');
        Character escape = format.getEscapeCharacter();
        assertNotNull(escape);
        assertEquals('\\', escape.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetEscapeCharacter_ExplicitWithEscapeCharacterNull() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat format = base.withEscape((Character) null);
        Character escape = format.getEscapeCharacter();
        assertNull(escape);
    }
}