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

public class CSVFormat_50_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharDifferent() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\"';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharSameAsDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character defaultEscape = format.getEscapeCharacter();
        // defaultEscape can be null, adjust test accordingly
        if (defaultEscape == null) {
            defaultEscape = '\\'; // fallback to a char that is valid
        }

        CSVFormat newFormat = format.withEscape(defaultEscape);

        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(defaultEscape, newFormat.getEscapeCharacter());
    }
}