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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_25_6Test {

    private void invokeValidate(CSVFormat format) throws Exception {
        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        validateMethod.invoke(format);
    }

    @Test
    @Timeout(8000)
    void testValidate_quoteCharEqualsDelimiter_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf(','), Quote.MINIMAL, null, null,
                false, true, "\r\n", null, null, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("quoteChar character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeEqualsDelimiter_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), Quote.MINIMAL, null, Character.valueOf(','),
                false, true, "\r\n", null, null, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("escape character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_commentStartEqualsDelimiter_throws() throws Exception {
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), Quote.MINIMAL, Character.valueOf(','),
                null, false, true, "\r\n", null, null, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("comment start character and the delimiter cannot be the same")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_quoteCharEqualsCommentStart_throws() throws Exception {
        CSVFormat format = new CSVFormat(';', Character.valueOf('#'), Quote.MINIMAL, Character.valueOf('#'),
                null, false, true, "\r\n", null, null, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("comment start character and the quoteChar cannot be the same")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeEqualsCommentStart_throws() throws Exception {
        CSVFormat format = new CSVFormat(';', Character.valueOf('"'), Quote.MINIMAL, Character.valueOf('!'),
                Character.valueOf('!'), false, true, "\r\n", null, null, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("comment start and the escape character cannot be the same")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_escapeNullAndQuotePolicyNone_throws() throws Exception {
        CSVFormat format = new CSVFormat(';', Character.valueOf('"'), Quote.NONE, null,
                null, false, true, "\r\n", null, null, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("No quotes mode set but no escape character is set")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_headerWithDuplicates_throws() throws Exception {
        String[] header = new String[] {"A", "B", "A"};
        CSVFormat format = new CSVFormat(',', Character.valueOf('"'), Quote.MINIMAL, null,
                null, false, true, "\r\n", null, header, false);
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> invokeValidate(format));
        if (!ex.getMessage().contains("The header contains duplicate names")) {
            fail("Unexpected exception message: " + ex.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testValidate_validConfiguration_noException() throws Exception {
        String[] header = new String[] {"A", "B", "C"};
        CSVFormat format = new CSVFormat(';', Character.valueOf('"'), Quote.MINIMAL, Character.valueOf('#'),
                Character.valueOf('\\'), false, true, "\r\n", null, header, false);
        // Should not throw
        invokeValidate(format);
    }
}