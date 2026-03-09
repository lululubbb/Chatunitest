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

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_47_2Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_char() {
        char commentChar = '#';
        CSVFormat result = csvFormatDefault.withCommentMarker(commentChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
        // Original instance should remain unchanged
        assertNull(csvFormatDefault.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Character_null() throws Exception {
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        CSVFormat result = (CSVFormat) method.invoke(csvFormatDefault, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getCommentMarker());
        assertNull(csvFormatDefault.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Character_nonNull() throws Exception {
        Method method = CSVFormat.class.getMethod("withCommentMarker", Character.class);
        Character commentChar = Character.valueOf('!');
        CSVFormat result = (CSVFormat) method.invoke(csvFormatDefault, commentChar);
        assertNotNull(result);
        assertEquals(commentChar, result.getCommentMarker());
        assertNull(csvFormatDefault.getCommentMarker());
    }
}