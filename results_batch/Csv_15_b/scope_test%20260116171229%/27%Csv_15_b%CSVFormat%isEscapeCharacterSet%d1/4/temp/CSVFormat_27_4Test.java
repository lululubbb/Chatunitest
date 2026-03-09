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

public class CSVFormat_27_4Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSetWhenEscapeCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscapeCharacterSet(), "Escape character should be set");
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSetWhenEscapeCharacterIsNull() throws Exception {
        // Create a CSVFormat instance with escape character set to non-null first
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');

        // Use reflection to access the private final escapeCharacter field
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);

        // Create a new CSVFormat instance with escapeCharacter set to null by invoking withEscape(null)
        CSVFormat formatWithNullEscape = format.withEscape((Character) null);

        // Assert that the new instance returns false for isEscapeCharacterSet
        assertFalse(formatWithNullEscape.isEscapeCharacterSet(), "Escape character should not be set");

        // Additionally, verify that the original instance still has escapeCharacter set
        assertTrue(format.isEscapeCharacterSet(), "Original escape character should still be set");
    }
}