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
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    public void testHashCode_allFieldsNonNullAndTrueFlags() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format = new CSVFormat(
                ',',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\r\n",
                "NULL",
                header,
                true,
                true
        );

        int expected = 1;
        int prime = 31;

        expected = prime * expected + ',';
        expected = prime * expected + QuoteMode.ALL.hashCode();
        expected = prime * expected + Character.valueOf('"').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "NULL".hashCode();
        expected = prime * expected + 1231; // ignoreSurroundingSpaces true
        expected = prime * expected + 1231; // ignoreEmptyLines true
        expected = prime * expected + 1231; // skipHeaderRecord true
        expected = prime * expected + "\r\n".hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_allFieldsNullOrFalseFlags() throws Exception {
        CSVFormat format = new CSVFormat(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                false,
                false
        );

        int expected = 1;
        int prime = 31;

        expected = prime * expected + ',';
        expected = prime * expected + 0; // quoteMode null
        expected = prime * expected + 0; // quoteCharacter null
        expected = prime * expected + 0; // commentMarker null
        expected = prime * expected + 0; // escapeCharacter null
        expected = prime * expected + 0; // nullString null
        expected = prime * expected + 1237; // ignoreSurroundingSpaces false
        expected = prime * expected + 1237; // ignoreEmptyLines false
        expected = prime * expected + 1237; // skipHeaderRecord false
        expected = prime * expected + 0; // recordSeparator null
        expected = prime * expected + Arrays.hashCode(null); // header null is 0

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_differentHeaders() {
        String[] header1 = new String[] {"a", "b"};
        String[] header2 = new String[] {"a", "c"};

        CSVFormat format1 = new CSVFormat(
                ',',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\r\n",
                "NULL",
                header1,
                true,
                true
        );

        CSVFormat format2 = new CSVFormat(
                ',',
                '"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\r\n",
                "NULL",
                header2,
                true,
                true
        );

        assertNotEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_reflectionInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method hashCodeMethod = CSVFormat.class.getDeclaredMethod("hashCode");
        hashCodeMethod.setAccessible(true);

        int hash1 = (int) hashCodeMethod.invoke(format);
        int hash2 = format.hashCode();

        assertEquals(hash1, hash2);
    }
}