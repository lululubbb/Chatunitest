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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

public class CSVFormat_12_1Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Using Mockito to mock the private field 'ignoreEmptyLines'
        CSVFormat spyCsvFormat = Mockito.spy(csvFormat);
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        ignoreEmptyLinesField.set(spyCsvFormat, true);

        // When
        boolean result = spyCsvFormat.getIgnoreEmptyLines();

        // Then
        assertEquals(true, result);
    }
}