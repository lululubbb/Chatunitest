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

class CSVFormat_28_3Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() throws Exception {
        // Create CSVFormat instance with nullString == null using withNullString(null)
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        // Use reflection to access the private final field nullString
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Confirm the field is null
        assertNull(nullStringField.get(format));

        // Call isNullStringSet and assert false
        assertFalse(format.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNonNull() throws Exception {
        // Create CSVFormat instance with nullString set
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Use reflection to access the private final field nullString
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Confirm the field is set to "NULL"
        assertEquals("NULL", nullStringField.get(format));

        // Call isNullStringSet and assert true
        assertTrue(format.isNullStringSet());
    }
}