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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatValidateTest {

    private Method validateMethod;
    private Class<?> quoteModeClass;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, ClassNotFoundException {
        // Access private validate method via reflection
        validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);

        // Load nested QuoteMode enum class via reflection
        quoteModeClass = Class.forName("org.apache.commons.csv.CSVFormat$QuoteMode");
    }

    private void invokeValidate(CSVFormat format) throws Throwable {
        try {
            validateMethod.invoke(format);
        } catch (InvocationTargetException e) {
            // unwrap exception thrown inside validate()
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterIsLineBreak() {
        CSVFormat format = CSVFormat.newFormat('\n'); // LF is line break
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("The delimiter cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('\"');
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsEscapeCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('\\').withEscape('\\');
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateDelimiterEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('#').withCommentMarker('#');
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateQuoteCharEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#').withCommentMarker('#');
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateEscapeCharacterEqualsCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('#').withCommentMarker('#');
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().contains("The comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidateNoEscapeCharacterAndQuoteModeNone() throws Exception {
        Object quoteModeNone = Enum.valueOf((Class<Enum>) quoteModeClass, "NONE");
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode((Enum<?>) quoteModeNone).withEscape((Character) null);
        Throwable ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertEquals("No quotes mode set but no escape character is set", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testValidateHeaderDuplicates() {
        String[] header = new String[] {"a", "b", "a"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> invokeValidate(format));
        assertTrue(ex.getMessage().startsWith("The header contains a duplicate entry"));
        assertTrue(ex.getMessage().contains("'a'"));
        assertTrue(ex.getMessage().contains(Arrays.toString(header)));
    }

    @Test
    @Timeout(8000)
    public void testValidateSuccess() {
        String[] header = new String[] {"a", "b", "c"};
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#').withEscape('\\').withHeader(header);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}