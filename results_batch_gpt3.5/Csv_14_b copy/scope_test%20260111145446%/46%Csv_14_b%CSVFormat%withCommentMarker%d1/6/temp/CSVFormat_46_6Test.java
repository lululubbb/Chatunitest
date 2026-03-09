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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_46_6Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_validInput() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withCommentMarker('#');

        // Then
        assertEquals('#', result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_invalidInput() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withCommentMarker('\n');
        });
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_noChange() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withCommentMarker(null);

        // Then
        assertEquals(null, result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_privateMethodInvocation() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        char commentMarker = '#';

        // When
        CSVFormat result = invokePrivateMethod(csvFormat, "withCommentMarker", Character.class, commentMarker);

        // Then
        assertEquals('#', result.getCommentMarker());
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(object, argument);
    }
}