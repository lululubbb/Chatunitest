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
import java.lang.reflect.Constructor;

public class CSVFormat_10_3Test {

    @Test
    @Timeout(8000)
    public void testGetHeader_WithNonNullHeader() throws Exception {
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat format = createCSVFormatWithHeader(header);

        String[] result = format.getHeader();

        assertNotNull(result);
        assertArrayEquals(header, result);
        assertNotSame(header, result); // ensure clone is returned, not original array
    }

    @Test
    @Timeout(8000)
    public void testGetHeader_WithNullHeader() throws Exception {
        CSVFormat format = createCSVFormatWithHeader(null);

        String[] result = format.getHeader();

        assertNull(result);
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Class<?> quoteClass = Class.forName("org.apache.commons.csv.CSVFormat$Quote");
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, quoteClass, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        // For quotePolicy, pass null of the correct type (the inner enum)
        Object quotePolicy = null;

        return constructor.newInstance(
                ',',                        // delimiter
                Character.valueOf('"'),     // quoteChar (boxed)
                quotePolicy,                // quotePolicy
                null,                       // commentStart
                null,                       // escape
                false,                      // ignoreSurroundingSpaces
                true,                       // ignoreEmptyLines
                "\r\n",                     // recordSeparator
                null,                       // nullString
                (Object) header,            // header (cast to Object to avoid varargs ambiguity)
                false                       // skipHeaderRecord
        );
    }
}