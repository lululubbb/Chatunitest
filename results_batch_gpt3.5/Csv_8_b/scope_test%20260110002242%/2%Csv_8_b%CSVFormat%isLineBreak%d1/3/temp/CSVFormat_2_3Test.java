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
import java.lang.reflect.Method;

public class CSVFormat_2_3Test {

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacterObject() throws Exception {
        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakMethod.setAccessible(true);

        // null Character -> false
        Boolean resultNull = (Boolean) isLineBreakMethod.invoke(null, (Object) null);
        assertFalse(resultNull);

        // Characters that are line breaks -> true
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\n')));

        // Characters that are not line breaks -> false
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf(' ')));
        assertFalse((Boolean) isLineBreakMethod.invoke(null, Character.valueOf('\t')));
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakChar() throws Exception {
        Method isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        // \r and \n are line breaks
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, (Object) Character.valueOf('\r')));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, (Object) Character.valueOf('\n')));

        // Other chars are not line breaks
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, (Object) Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, (Object) Character.valueOf(' ')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, (Object) Character.valueOf('\t')));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, (Object) Character.valueOf(',')));
    }
}