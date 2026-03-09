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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_9_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker() {
        // Given
        Character expected = '#';
        assertEquals(expected, csvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarkerWhenNull() {
        // Given
        csvFormat = CSVFormat.DEFAULT.withCommentMarker(null);
        Character expected = null;
        assertEquals(expected, csvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() {
        // Given
        assertTrue(csvFormat.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetUsingReflection() throws Exception {
        // Given
        Method method = CSVFormat.class.getDeclaredMethod("isCommentMarkerSet");
        method.setAccessible(true);

        // When
        boolean result = (boolean) method.invoke(csvFormat);

        // Then
        assertTrue(result);
    }
}