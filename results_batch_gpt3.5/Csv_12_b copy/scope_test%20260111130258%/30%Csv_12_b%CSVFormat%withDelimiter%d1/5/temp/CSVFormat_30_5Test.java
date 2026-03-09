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
import org.mockito.Mockito;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_30_5Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        // Given
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);
        boolean isLineBreakResult = (boolean) isLineBreakMethod.invoke(null, delimiter);

        if (isLineBreakResult) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> csvFormat.withDelimiter(delimiter));
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        } else {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            CSVFormat newCsvFormat = constructor.newInstance(delimiter, csvFormat.getQuoteCharacter(), csvFormat.getQuoteMode(), csvFormat.getCommentMarker(), csvFormat.getEscapeCharacter(), csvFormat.getIgnoreSurroundingSpaces(), csvFormat.getIgnoreEmptyLines(), csvFormat.getRecordSeparator(), csvFormat.getNullString(), csvFormat.getHeader(), csvFormat.getSkipHeaderRecord(), csvFormat.getAllowMissingColumnNames());

            Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
            delimiterField.setAccessible(true);
            char newDelimiter = (char) delimiterField.get(newCsvFormat);

            // Then
            assertEquals(delimiter, newDelimiter);
        }
    }
}