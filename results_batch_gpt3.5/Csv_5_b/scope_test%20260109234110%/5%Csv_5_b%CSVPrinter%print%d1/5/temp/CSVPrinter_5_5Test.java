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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinterPrintTest {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.initMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    private void invokePrint(Object object, CharSequence value, int offset, int len) throws Throwable {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        try {
            printMethod.invoke(printer, object, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordTrue_Quoting() throws Throwable {
        // Arrange
        when(format.getDelimiter()).thenReturn(',');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        // newRecord is true initially, so no delimiter appended
        // We test printAndQuote is called by spying on printer

        CSVPrinter spyPrinter = spy(printer);
        setField(spyPrinter, "newRecord", true);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        // Act
        printMethod.invoke(spyPrinter, "test", "value", 0, 5);

        // Assert
        verify(out, never()).append(anyChar());
        // Instead of verifying private call, verify out.append called at least once (printAndQuote uses out.append)
        verify(out, atLeastOnce()).append(any(CharSequence.class));
        // newRecord should be false after print
        boolean newRecord = (boolean) getField(spyPrinter, "newRecord");
        assert !newRecord;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Quoting() throws Throwable {
        when(format.getDelimiter()).thenReturn(';');
        when(format.isQuoting()).thenReturn(true);
        when(format.isEscaping()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);
        setField(spyPrinter, "newRecord", false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyPrinter, 123, "value", 0, 5);

        verify(out).append(';');
        verify(out, atLeastOnce()).append(any(CharSequence.class));
        boolean newRecord = (boolean) getField(spyPrinter, "newRecord");
        assert !newRecord;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_Escaping() throws Throwable {
        when(format.getDelimiter()).thenReturn('|');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(true);

        CSVPrinter spyPrinter = spy(printer);
        setField(spyPrinter, "newRecord", false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        printMethod.invoke(spyPrinter, null, "escapeValue", 0, 11);

        verify(out).append('|');
        // printAndEscape should call out.append at least once
        verify(out, atLeastOnce()).append(any(CharSequence.class));
        boolean newRecord = (boolean) getField(spyPrinter, "newRecord");
        assert !newRecord;
    }

    @Test
    @Timeout(8000)
    void testPrint_NewRecordFalse_NoQuotingNoEscaping() throws Throwable {
        when(format.getDelimiter()).thenReturn('\t');
        when(format.isQuoting()).thenReturn(false);
        when(format.isEscaping()).thenReturn(false);

        CSVPrinter spyPrinter = spy(printer);
        setField(spyPrinter, "newRecord", false);

        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        String val = "plainValue";
        printMethod.invoke(spyPrinter, val, val, 0, val.length());

        verify(out).append('\t');
        verify(out).append(val, 0, val.length());
        boolean newRecord = (boolean) getField(spyPrinter, "newRecord");
        assert !newRecord;
    }

    // Helper methods for reflection field access
    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getField(Object target, String fieldName) {
        try {
            Field field = CSVPrinter.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}