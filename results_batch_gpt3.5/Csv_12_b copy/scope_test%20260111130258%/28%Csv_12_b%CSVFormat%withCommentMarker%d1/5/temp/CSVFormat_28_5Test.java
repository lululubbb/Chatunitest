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

public class CSVFormat_28_5Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Create a CSVFormat object
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Define the comment marker
        char commentMarker = '#';

        // Call the withCommentMarker method
        CSVFormat updatedFormat = csvFormat.withCommentMarker(commentMarker);

        // Use reflection to access the private field commentMarker in CSVFormat
        Character actualCommentMarker = null;
        try {
            java.lang.reflect.Field field = CSVFormat.class.getDeclaredField("commentMarker");
            field.setAccessible(true);
            actualCommentMarker = (Character) field.get(updatedFormat);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Assert the updated comment marker
        assertEquals(Character.valueOf(commentMarker), actualCommentMarker);
    }

    // Add more test cases for other methods if needed
}