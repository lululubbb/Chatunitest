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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_30_1Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_ValidDelimiter_ReturnsNewCSVFormat() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(';');

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withDelimiter", char.class);
        method.setAccessible(true);
        CSVFormat newCsvFormat = (CSVFormat) method.invoke(csvFormat, delimiter);

        // Then
        assertNotSame(csvFormat, newCsvFormat);
        assertEquals(delimiter, newCsvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_InvalidDelimiter_ThrowsIllegalArgumentException() {
        // Given
        char lineBreakDelimiter = '\n';
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withDelimiter(lineBreakDelimiter);
        });

        // Then
        assertEquals("The delimiter cannot be a line break", exception.getMessage());
    }
}