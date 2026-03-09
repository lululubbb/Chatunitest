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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_48_6Test {

    @Test
    @Timeout(8000)
    public void testWithEscape() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withEscape(escape);

        // Then
        assertNotNull(updatedFormat);
        assertEquals(escape, updatedFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_PrivateMethodInvocation() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = spy(csvFormat);

        // When
        spyFormat.withEscape(escape);

        // Then
        verify(spyFormat, times(1)).withEscape(Character.valueOf(escape));
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_PrivateMethodInvocationReflectively() throws Exception {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = spy(csvFormat);

        // When
        spyFormat.withEscape(escape);

        // Then
        Method method = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        method.setAccessible(true);
        method.invoke(spyFormat, Character.valueOf(escape));
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_PrivateMethodInvocationReflectively2() throws Exception {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = spy(csvFormat);

        // When
        spyFormat.withEscape(escape);

        // Then
        Method method = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        method.setAccessible(true);
        method.invoke(spyFormat, Character.valueOf(escape));
    }

}