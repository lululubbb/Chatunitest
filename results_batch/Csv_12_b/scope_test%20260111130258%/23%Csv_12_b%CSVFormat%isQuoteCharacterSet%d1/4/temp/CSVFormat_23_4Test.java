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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_4Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean result = invokeIsQuoteCharacterSet(csvFormat);

        // Then
        assertFalse(result);
    }

    private boolean invokeIsQuoteCharacterSet(CSVFormat csvFormat) {
        try {
            Method method = CSVFormat.class.getDeclaredMethod("isQuoteCharacterSet");
            method.setAccessible(true);
            return (boolean) method.invoke(csvFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}