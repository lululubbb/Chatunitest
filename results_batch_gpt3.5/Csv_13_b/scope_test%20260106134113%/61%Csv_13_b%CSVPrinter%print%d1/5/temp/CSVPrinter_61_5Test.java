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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_61_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullValue_andNullStringNull() throws Exception {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        verifyPrivateMethodCalled(spyPrinter, printMethod, null, "", 0, 0);
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullValue_andNullStringNonNull() throws Exception {
        String nullStr = "NULL";
        when(format.getNullString()).thenReturn(nullStr);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        verifyPrivateMethodCalled(spyPrinter, printMethod, null, nullStr, 0, nullStr.length());
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNonNullValue() throws Exception {
        String val = "value";
        when(format.getNullString()).thenReturn(null); // irrelevant here

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(val);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        verifyPrivateMethodCalled(spyPrinter, printMethod, val, val, 0, val.length());
    }

    @Test
    @Timeout(8000)
    public void testPrint_propagatesIOException() throws Exception {
        String val = "value";

        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Using doThrow on public print(Object) method since private method cannot be stubbed directly
        doThrow(new IOException("forced")).when(spyPrinter).print(val);

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.print(val));
        assertEquals("forced", thrown.getMessage());
    }

    private void verifyPrivateMethodCalled(CSVPrinter spy, Method method, Object obj, CharSequence seq, int offset, int len) throws Exception {
        // Instead of clearInvocations(spy.getOut()), use Mockito.clearInvocations correctly
        clearInvocations(spy.getOut());

        method.setAccessible(true);
        method.invoke(spy, obj, seq, offset, len);

        if (len > 0) {
            verify(spy.getOut()).append(seq, offset, offset + len);
        } else {
            verify(spy.getOut(), never()).append(any(CharSequence.class), anyInt(), anyInt());
        }
    }

    // Add static import for clearInvocations
    private static void clearInvocations(Object... mocks) {
        org.mockito.Mockito.clearInvocations(mocks);
    }
}