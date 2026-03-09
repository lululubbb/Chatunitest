package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;

import org.junit.jupiter.api.Test;

public class CSVFormat_28_3Test {

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
    public void testWithCommentMarkerUsingReflection() throws Exception {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result;
        try {
            result = invokeWithCommentMarkerUsingReflection(csvFormat, commentMarker);
        } catch (NoSuchMethodException e) {
            // Handle exception
            result = null;
        }

        // Then
        assertEquals(Character.valueOf(commentMarker), result.getCommentMarker());
    }

    private CSVFormat invokeWithCommentMarkerUsingReflection(CSVFormat csvFormat, char commentMarker)
            throws Exception {
        Class<?>[] parameterTypes = {char.class};
        Object[] parameters = {commentMarker};
        java.lang.reflect.Method method = CSVFormat.class.getDeclaredMethod("withCommentMarker", parameterTypes);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, parameters);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker() {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = new CSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, false, false);

        // When
        Character result = csvFormat.getCommentMarker();

        // Then
        assertEquals(Character.valueOf(commentMarker), result);
    }
}