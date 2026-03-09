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

public class CSVFormat_10_4Test {

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameInstance() throws Exception {
        String[] headers = new String[] {"a", "b", "c"};
        CSVFormat format = createCSVFormatWithHeader(headers);

        // Use reflection to invoke public method getHeader
        Method getHeaderMethod = CSVFormat.class.getMethod("getHeader");
        String[] returnedHeader = (String[]) getHeaderMethod.invoke(format);

        assertNotNull(returnedHeader);
        assertArrayEquals(headers, returnedHeader);
        assertNotSame(headers, returnedHeader);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        CSVFormat format = createCSVFormatWithHeader(null);

        Method getHeaderMethod = CSVFormat.class.getMethod("getHeader");
        String[] returnedHeader = (String[]) getHeaderMethod.invoke(format);

        assertNull(returnedHeader);
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        // Use reflection to access private constructor
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Object.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        // Provide dummy values for other parameters
        return constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar
                null, // quotePolicy (changed to Object.class, so null is fine)
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                true, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // nullString
                header, // header
                false // skipHeaderRecord
        );
    }
}