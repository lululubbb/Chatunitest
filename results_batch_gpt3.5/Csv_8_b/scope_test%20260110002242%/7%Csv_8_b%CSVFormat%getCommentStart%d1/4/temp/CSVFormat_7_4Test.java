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
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_7_4Test {

    @Test
    @Timeout(8000)
    void testGetCommentStartWhenNull() throws Exception {
        // Create a new CSVFormat instance with commentStart set to non-null
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');

        // forcibly set commentStart to null via reflection to ensure test correctness
        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);

        // Remove final modifier from the field to allow modification
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentStartField, commentStartField.getModifiers() & ~Modifier.FINAL);

        // Since commentStart is a field of the instance, set it on the instance
        commentStartField.set(format, null);

        assertNull(format.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testGetCommentStartWhenSetChar() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        assertEquals(Character.valueOf('#'), format.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testGetCommentStartWhenSetCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart(Character.valueOf(';'));
        assertEquals(Character.valueOf(';'), format.getCommentStart());
    }
}