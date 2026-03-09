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

public class CSVFormat_1_3Test {

    @Test
    @Timeout(8000)
    void testIsLineBreak() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        char lf = '\n';
        char cr = '\r';
        char otherChar = 'a';

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);

        // When
        boolean isLFLineBreak = (boolean) isLineBreakMethod.invoke(null, lf);
        boolean isCRLineBreak = (boolean) isLineBreakMethod.invoke(null, cr);
        boolean isOtherCharLineBreak = (boolean) isLineBreakMethod.invoke(null, otherChar);

        // Then
        assertTrue(isLFLineBreak);
        assertTrue(isCRLineBreak);
        assertFalse(isOtherCharLineBreak);
    }
}