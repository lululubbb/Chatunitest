package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_32_3Test {

    private PrintStream originalOut;
    private PrintStream mockOut;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        mockOut = mock(PrintStream.class);
        System.setOut(mockOut);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @Timeout(8000)
    public void testPrinter_DefaultCSVFormat_PrinterCreated() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.printer();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    public void testPrinter_ExcelCSVFormat_PrinterCreated() throws IOException {
        CSVFormat format = CSVFormat.EXCEL;
        CSVPrinter printer = format.printer();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    public void testPrinter_InformixUnloadCSVFormat_PrinterCreated() throws IOException {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        CSVPrinter printer = format.printer();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    public void testPrinter_MySQLCSVFormat_PrinterCreated() throws IOException {
        CSVFormat format = CSVFormat.MYSQL;
        CSVPrinter printer = format.printer();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    public void testPrinter_ThrowsIOException() {
        assertDoesNotThrow(() -> CSVFormat.DEFAULT.printer());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakChar.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertFalse((boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((boolean) isLineBreakCharacter.invoke(null, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testNewFormat_StaticFactory() {
        CSVFormat format = CSVFormat.newFormat(',');
        assertNotNull(format);
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testValueOf_StaticFactory() {
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertNotNull(format);
        assertEquals(CSVFormat.DEFAULT, format);
    }
}