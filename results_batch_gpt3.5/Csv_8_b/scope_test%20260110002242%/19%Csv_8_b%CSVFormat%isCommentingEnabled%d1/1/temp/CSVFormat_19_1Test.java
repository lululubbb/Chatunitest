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

class CSVFormat_19_1Test {

    @Test
    @Timeout(8000)
    void testIsCommentingEnabledWhenCommentStartIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat modifiedFormat = format.withCommentStart((Character) null);
        // Use reflection to forcibly set commentStart to null
        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);

        // The field is final, so remove final modifier using reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(commentStartField, commentStartField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        commentStartField.set(modifiedFormat, null);

        assertFalse(modifiedFormat.isCommentingEnabled());
    }

    @Test
    @Timeout(8000)
    void testIsCommentingEnabledWhenCommentStartIsChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat modifiedFormat = format.withCommentStart('#');
        assertTrue(modifiedFormat.isCommentingEnabled());
    }

    @Test
    @Timeout(8000)
    void testIsCommentingEnabledWhenCommentStartIsCharacterObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat modifiedFormat = format.withCommentStart(Character.valueOf('!'));
        assertTrue(modifiedFormat.isCommentingEnabled());
    }
}