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
import org.junit.jupiter.api.Test;

class CSVFormat_11_3Test {

    @Test
    @Timeout(8000)
    void testGetEscapeCharacterWhenEscapeIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertNull(format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacterWhenEscapeIsSet() {
        char escapeChar = '\\';
        CSVFormat format = CSVFormat.DEFAULT.withEscape(escapeChar);
        assertEquals(Character.valueOf(escapeChar), format.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeCharacterFromPredefinedFormats() {
        // INFORMIX_UNLOAD has escape character BACKSLASH
        assertEquals(Character.valueOf('\\'), CSVFormat.INFORMIX_UNLOAD.getEscapeCharacter());

        // MYSQL has escape character BACKSLASH
        assertEquals(Character.valueOf('\\'), CSVFormat.MYSQL.getEscapeCharacter());

        // EXCEL has escape character null
        assertNull(CSVFormat.EXCEL.getEscapeCharacter());

        // RFC4180 has escape character null
        assertNull(CSVFormat.RFC4180.getEscapeCharacter());

        // TDF has escape character null
        assertNull(CSVFormat.TDF.getEscapeCharacter());
    }
}