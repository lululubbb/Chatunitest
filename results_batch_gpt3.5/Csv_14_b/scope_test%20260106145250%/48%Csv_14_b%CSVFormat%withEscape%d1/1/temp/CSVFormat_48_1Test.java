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

public class CSVFormat_48_1Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_withVariousChars() {
        // Test with normal escape character
        CSVFormat format1 = CSVFormat.DEFAULT.withEscape('\\');
        assertNotNull(format1);
        assertEquals(Character.valueOf('\\'), format1.getEscapeCharacter());

        // Test with quote character as escape
        CSVFormat format2 = CSVFormat.DEFAULT.withEscape('"');
        assertNotNull(format2);
        assertEquals(Character.valueOf('"'), format2.getEscapeCharacter());

        // Test with comma as escape
        CSVFormat format3 = CSVFormat.DEFAULT.withEscape(',');
        assertNotNull(format3);
        assertEquals(Character.valueOf(','), format3.getEscapeCharacter());

        // Test with non-printable character as escape
        // Use withEscape(Character) with null char '\0' boxed to Character
        CSVFormat format4 = CSVFormat.DEFAULT.withEscape((Character) '\0');
        assertNotNull(format4);
        assertEquals(Character.valueOf('\0'), format4.getEscapeCharacter());

        // Test with digit character as escape
        CSVFormat format5 = CSVFormat.DEFAULT.withEscape('5');
        assertNotNull(format5);
        assertEquals(Character.valueOf('5'), format5.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_nullEscapeCharacter() {
        // Use the withEscape(Character) method with null to simulate no escape
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertNotNull(format);
        assertNull(format.getEscapeCharacter());
    }
}