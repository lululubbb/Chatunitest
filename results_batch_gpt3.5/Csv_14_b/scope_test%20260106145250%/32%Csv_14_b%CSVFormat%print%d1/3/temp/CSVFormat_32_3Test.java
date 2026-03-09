package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_PathToFile_Success() throws IOException, Exception {
        Path mockPath = mock(Path.class);
        File mockFile = mock(File.class);
        Charset charset = Charset.defaultCharset();

        when(mockPath.toFile()).thenReturn(mockFile);

        // Create a spy of CSVFormat.DEFAULT using reflection to bypass final fields
        CSVFormat spyFormat = createSpyCSVFormat(csvFormat);

        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        // Use doReturn().when() to stub print(File, Charset)
        doReturn(mockPrinter).when(spyFormat).print(eq(mockFile), eq(charset));

        CSVPrinter printer = spyFormat.print(mockPath, charset);

        assertNotNull(printer);
        assertEquals(mockPrinter, printer);

        verify(mockPath).toFile();
        verify(spyFormat).print(mockFile, charset);
    }

    private CSVFormat createSpyCSVFormat(CSVFormat original) throws Exception {
        Class<?> cls = CSVFormat.class;

        Field delimiterField = cls.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(original);

        Field quoteCharacterField = cls.getDeclaredField("quoteChar");
        quoteCharacterField.setAccessible(true);
        Character quoteChar = (Character) quoteCharacterField.get(original);

        Field quoteModeField = cls.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(original);

        Field commentMarkerField = cls.getDeclaredField("commentStart");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(original);

        Field escapeCharacterField = cls.getDeclaredField("escape");
        escapeCharacterField.setAccessible(true);
        Character escapeChar = (Character) escapeCharacterField.get(original);

        Field ignoreSurroundingSpacesField = cls.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(original);

        Field ignoreEmptyLinesField = cls.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(original);

        Field recordSeparatorField = cls.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(original);

        Field nullStringField = cls.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(original);

        Field headerCommentsField = cls.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(original);

        Field headerField = cls.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(original);

        Field skipHeaderRecordField = cls.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(original);

        Field allowMissingColumnNamesField = cls.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(original);

        Field ignoreHeaderCaseField = cls.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(original);

        Field trimField = cls.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = trimField.getBoolean(original);

        Field trailingDelimiterField = cls.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(original);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class
        );
        constructor.setAccessible(true);

        CSVFormat newInstance = constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeChar,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter
        );

        return spy(newInstance);
    }

    @Test
    @Timeout(8000)
    void testPrint_FileCharset_Success() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();
        Charset charset = Charset.defaultCharset();

        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'');
        CSVPrinter printer = format.print(tempFile, charset);

        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_FileCharset_IOException() {
        File nonWritableFile = new File("/invalid_path/test.csv");
        Charset charset = Charset.defaultCharset();

        CSVFormat format = CSVFormat.DEFAULT;

        assertThrows(IOException.class, () -> format.print(nonWritableFile, charset));
    }

    @Test
    @Timeout(8000)
    void testPrint_Appendable_Success() throws IOException {
        Appendable appendable = new StringBuilder();

        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        CSVPrinter printer = format.print(appendable);

        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_ObjectValue_NewRecordFalse() throws IOException {
        Appendable appendable = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;

        format.print("value", appendable, false);

        assertTrue(appendable.toString().contains("value"));
    }

    @Test
    @Timeout(8000)
    void testPrint_ObjectValue_NewRecordTrue() throws IOException {
        Appendable appendable = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;

        format.print("value", appendable, true);

        assertTrue(appendable.toString().contains("value"));
    }
}