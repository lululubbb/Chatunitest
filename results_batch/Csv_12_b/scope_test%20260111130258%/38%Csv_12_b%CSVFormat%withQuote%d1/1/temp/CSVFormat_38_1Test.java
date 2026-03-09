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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class CSVFormat_38_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        // Given
        char quoteChar = '"';
        CSVFormat expectedFormat = CSVFormat.DEFAULT.withQuote(quoteChar);

        // When
        CSVFormat actualFormat = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteUsingReflection() throws Exception {
        // Given
        char quoteChar = '"';
        CSVFormat expectedFormat = CSVFormat.DEFAULT.withQuote(quoteChar);

        // When
        CSVFormat actualFormat = invokeWithQuoteUsingReflection(csvFormat, quoteChar);

        // Then
        assertEquals(expectedFormat, actualFormat);
    }

    private CSVFormat invokeWithQuoteUsingReflection(CSVFormat csvFormat, char quoteChar) throws Exception {
        CSVFormat instance = new CSVFormat('\0', null, null, null, null, false, false, null, null, null, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(instance, quoteChar);
    }
}