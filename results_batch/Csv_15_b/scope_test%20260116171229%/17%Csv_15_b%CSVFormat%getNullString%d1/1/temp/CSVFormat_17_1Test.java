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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

public class CSVFormat_17_1Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatWithNullString;
    private CSVFormat csvFormatWithNullStringEmpty;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatWithNullString = CSVFormat.DEFAULT.withNullString("NULL");
        csvFormatWithNullStringEmpty = CSVFormat.DEFAULT.withNullString("");
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_Default() {
        String nullStr = csvFormatDefault.getNullString();
        // DEFAULT has null nullString
        assertNull(nullStr);
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_WithNullString() {
        String nullStr = csvFormatWithNullString.getNullString();
        assertEquals("NULL", nullStr);
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_WithEmptyNullString() {
        String nullStr = csvFormatWithNullStringEmpty.getNullString();
        assertEquals("", nullStr);
    }

    @Test
    @Timeout(8000)
    public void testGetNullString_Reflection() throws Exception {
        Method method = CSVFormat.class.getMethod("getNullString");

        String resultDefault = (String) method.invoke(csvFormatDefault);
        assertNull(resultDefault);

        String resultWithNull = (String) method.invoke(csvFormatWithNullString);
        assertEquals("NULL", resultWithNull);

        String resultWithEmpty = (String) method.invoke(csvFormatWithNullStringEmpty);
        assertEquals("", resultWithEmpty);
    }
}