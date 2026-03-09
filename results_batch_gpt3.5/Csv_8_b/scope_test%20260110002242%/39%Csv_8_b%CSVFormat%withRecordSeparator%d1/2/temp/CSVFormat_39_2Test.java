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
import java.lang.reflect.Method;

public class CSVFormat_39_2Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_String() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Call withRecordSeparator(String) with a normal string
        CSVFormat changed = original.withRecordSeparator("\n");
        assertNotNull(changed);
        assertNotSame(original, changed);
        assertEquals("\n", changed.getRecordSeparator());

        // Call withRecordSeparator(String) with empty string
        CSVFormat changedEmpty = original.withRecordSeparator("");
        assertNotNull(changedEmpty);
        assertNotSame(original, changedEmpty);
        assertEquals("", changedEmpty.getRecordSeparator());

        // Call withRecordSeparator(String) with null
        CSVFormat changedNull = original.withRecordSeparator((String) null);
        assertNotNull(changedNull);
        assertNotSame(original, changedNull);
        assertNull(changedNull.getRecordSeparator());

        // Using reflection to invoke getRecordSeparator method (which is public)
        Method getRecordSeparator = CSVFormat.class.getMethod("getRecordSeparator");
        String rs = (String) getRecordSeparator.invoke(changed);
        assertEquals("\n", rs);
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Call withRecordSeparator(char)
        CSVFormat changed = original.withRecordSeparator('\r');
        assertNotNull(changed);
        assertNotSame(original, changed);
        assertEquals("\r", changed.getRecordSeparator());

        // Call withRecordSeparator with line feed char
        CSVFormat changedLF = original.withRecordSeparator('\n');
        assertEquals("\n", changedLF.getRecordSeparator());

        // Call withRecordSeparator with tab char (unusual but allowed)
        CSVFormat changedTab = original.withRecordSeparator('\t');
        assertEquals("\t", changedTab.getRecordSeparator());

        // Using reflection to invoke getRecordSeparator method (which is public)
        Method getRecordSeparator = CSVFormat.class.getMethod("getRecordSeparator");
        String rs = (String) getRecordSeparator.invoke(changedTab);
        assertEquals("\t", rs);
    }
}