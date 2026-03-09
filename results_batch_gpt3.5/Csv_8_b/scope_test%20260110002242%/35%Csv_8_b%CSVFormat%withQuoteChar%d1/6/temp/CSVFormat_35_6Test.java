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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_35_6Test {

    @Test
    @Timeout(8000)
    void testWithQuoteChar_withVariousChars() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal quote char
        CSVFormat format1 = baseFormat.withQuoteChar(Character.valueOf('"'));
        assertNotNull(format1);
        assertEquals(Character.valueOf('"'), format1.getQuoteChar());
        assertNotSame(baseFormat, format1);

        // Test with a different quote char
        CSVFormat format2 = baseFormat.withQuoteChar(Character.valueOf('\''));
        assertNotNull(format2);
        assertEquals(Character.valueOf('\''), format2.getQuoteChar());
        assertNotSame(baseFormat, format2);

        // Test with quote char that is same as current quote char (should still return new instance)
        CSVFormat format3 = format1.withQuoteChar(Character.valueOf('"'));
        assertNotNull(format3);
        assertEquals(Character.valueOf('"'), format3.getQuoteChar());
        assertNotSame(format1, format3);

        // Test with quote char zero '\0' (edge case)
        CSVFormat format4 = baseFormat.withQuoteChar(Character.valueOf('\0'));
        assertNotNull(format4);
        assertEquals(Character.valueOf('\0'), format4.getQuoteChar());
        assertNotSame(baseFormat, format4);

        // Test with quote char that is a whitespace
        CSVFormat format5 = baseFormat.withQuoteChar(Character.valueOf(' '));
        assertNotNull(format5);
        assertEquals(Character.valueOf(' '), format5.getQuoteChar());
        assertNotSame(baseFormat, format5);
    }

}