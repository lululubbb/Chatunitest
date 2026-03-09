package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class CSVPrinter_63_2Test {

    private CSVPrinter csvPrinter;
    private Appendable out;
    private CSVFormat format;

    private Method printAndEscapeMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        csvPrinter = new CSVPrinter(out, format);

        printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "HelloWorld";
        int offset = 0;
        int len = input.length();

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        verify(out).append(input, offset, offset + len);
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "Hello,World";
        int offset = 0;
        int len = input.length();

        // Delimiter is ','
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        // Expected calls:
        // append("Hello", 0, 5)
        // append('\\')
        // append(',')
        // append("World", 6, 11)

        ArgumentCaptor<CharSequence> csCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> intCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> intCaptor2 = ArgumentCaptor.forClass(Integer.class);

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("Hello", csCaptor.getValue());
        assertEquals(0, intCaptor1.getValue());
        assertEquals(5, intCaptor2.getValue());

        verify(out).append('\\');
        verify(out).append(',');

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("World", csCaptor.getValue());
        assertEquals(6, intCaptor1.getValue());
        assertEquals(11, intCaptor2.getValue());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "abc\\def";
        int offset = 0;
        int len = input.length();

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        // Expected calls:
        // append("abc", 0, 3)
        // append('\\')
        // append('\\')
        // append("def", 4, 7)

        ArgumentCaptor<CharSequence> csCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> intCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> intCaptor2 = ArgumentCaptor.forClass(Integer.class);

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("abc", csCaptor.getValue());
        assertEquals(0, intCaptor1.getValue());
        assertEquals(3, intCaptor2.getValue());

        verify(out).append('\\');
        verify(out).append('\\');

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("def", csCaptor.getValue());
        assertEquals(4, intCaptor1.getValue());
        assertEquals(7, intCaptor2.getValue());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithCR() throws Throwable {
        String input = "abc\rdef";
        int offset = 0;
        int len = input.length();

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        // Expected calls:
        // append("abc", 0, 3)
        // append('\\')
        // append('r')
        // append("def", 4, 7)

        ArgumentCaptor<CharSequence> csCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> intCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> intCaptor2 = ArgumentCaptor.forClass(Integer.class);

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("abc", csCaptor.getValue());
        assertEquals(0, intCaptor1.getValue());
        assertEquals(3, intCaptor2.getValue());

        verify(out).append('\\');
        verify(out).append('r');

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("def", csCaptor.getValue());
        assertEquals(4, intCaptor1.getValue());
        assertEquals(7, intCaptor2.getValue());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithLF() throws Throwable {
        String input = "abc\ndef";
        int offset = 0;
        int len = input.length();

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        // Expected calls:
        // append("abc", 0, 3)
        // append('\\')
        // append('n')
        // append("def", 4, 7)

        ArgumentCaptor<CharSequence> csCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> intCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> intCaptor2 = ArgumentCaptor.forClass(Integer.class);

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("abc", csCaptor.getValue());
        assertEquals(0, intCaptor1.getValue());
        assertEquals(3, intCaptor2.getValue());

        verify(out).append('\\');
        verify(out).append('n');

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals("def", csCaptor.getValue());
        assertEquals(4, intCaptor1.getValue());
        assertEquals(7, intCaptor2.getValue());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_EmptyInput() throws Throwable {
        String input = "";
        int offset = 0;
        int len = 0;

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        verifyNoInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "123,456,789";
        int offset = 4; // start at '4'
        int len = 5;    // "456,7"

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        printAndEscapeMethod.invoke(csvPrinter, input, offset, len);

        // input substring: "456,7"
        // expected:
        // append("456", 4, 7)
        // append('\\')
        // append(',')
        // append("7", 8, 9)

        ArgumentCaptor<CharSequence> csCaptor = ArgumentCaptor.forClass(CharSequence.class);
        ArgumentCaptor<Integer> intCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> intCaptor2 = ArgumentCaptor.forClass(Integer.class);

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals(input, csCaptor.getValue());
        assertEquals(4, intCaptor1.getValue());
        assertEquals(7, intCaptor2.getValue());

        verify(out).append('\\');
        verify(out).append(',');

        verify(out).append(csCaptor.capture(), intCaptor1.capture(), intCaptor2.capture());
        assertEquals(input, csCaptor.getValue());
        assertEquals(8, intCaptor1.getValue());
        assertEquals(9, intCaptor2.getValue());

        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_ThrowsIOException() throws Throwable {
        String input = "abc,def";
        int offset = 0;
        int len = input.length();

        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscapeCharacter()).thenReturn('\\');

        doThrow(new IOException("append failed")).when(out).append(any(CharSequence.class), anyInt(), anyInt());

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> printAndEscapeMethod.invoke(csvPrinter, input, offset, len));
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("append failed", thrown.getCause().getMessage());
    }
}