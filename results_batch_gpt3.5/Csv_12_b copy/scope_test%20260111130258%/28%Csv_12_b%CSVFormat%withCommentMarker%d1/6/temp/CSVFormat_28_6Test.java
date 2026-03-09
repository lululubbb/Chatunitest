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
import java.io.Reader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_28_6Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertEquals(Character.valueOf(commentMarker), updatedFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Null() {
        // Given
        char commentMarker = '\0';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertNull(updatedFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Object() {
        // Given
        Character commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertEquals(commentMarker, updatedFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Object_Null() {
        // Given
        Character commentMarker = null;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertNull(updatedFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_PrivateMethodInvocation() throws Exception {
        // Given
        char commentMarker = '#';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = invokePrivateMethod(csvFormat, "withCommentMarker", Character.class, commentMarker);

        // Then
        assertEquals(Character.valueOf(commentMarker), updatedFormat.getCommentMarker());
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(object, argument);
    }
}