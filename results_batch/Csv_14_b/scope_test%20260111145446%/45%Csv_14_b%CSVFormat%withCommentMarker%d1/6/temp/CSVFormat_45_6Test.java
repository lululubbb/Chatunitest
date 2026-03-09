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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_45_6Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertEquals(Character.valueOf(commentMarker), result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_WithMockito() throws Exception {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat csvFormatMock = mock(CSVFormat.class);

        // Mocking private method isLineBreak
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        when(method.invoke(csvFormatMock, commentMarker)).thenReturn(true);
        when(csvFormatMock.withCommentMarker(Character.valueOf(commentMarker))).thenCallRealMethod();

        // When
        CSVFormat result = csvFormatMock.withCommentMarker(commentMarker);

        // Then
        assertEquals(Character.valueOf(commentMarker), result.getCommentMarker());
    }
}