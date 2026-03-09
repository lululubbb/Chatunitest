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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() throws Exception {
        // Copy CSVFormat.DEFAULT via reflection to allow spying
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = delimiterField.getChar(defaultFormat);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(defaultFormat);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentStart");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        Character escape = (Character) escapeField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(defaultFormat);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(defaultFormat);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(defaultFormat);

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(defaultFormat);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(defaultFormat);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(defaultFormat);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = trimField.getBoolean(defaultFormat);

        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(defaultFormat);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class,
                boolean.class, boolean.class,
                String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class
        );
        constructor.setAccessible(true);
        csvFormat = constructor.newInstance(
                delimiter,
                quoteChar,
                quoteMode,
                commentMarker,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter
        );
    }

    @Test
    @Timeout(8000)
    void testPrint_WithPathAndCharset_DelegatesToPrintFileCharset() throws IOException {
        Path mockPath = mock(Path.class);
        File mockFile = mock(File.class);
        Charset charset = Charset.defaultCharset();

        when(mockPath.toFile()).thenReturn(mockFile);
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        CSVPrinter expectedPrinter = mock(CSVPrinter.class);
        doReturn(expectedPrinter).when(spyFormat).print(any(File.class), eq(charset));

        CSVPrinter actualPrinter = spyFormat.print(mockPath, charset);

        assertSame(expectedPrinter, actualPrinter);
        verify(mockPath).toFile();
        verify(spyFormat).print(mockFile, charset);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithNullPath_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.print((Path) null, Charset.defaultCharset()));
    }

    @Test
    @Timeout(8000)
    void testPrint_WithNullCharset_ThrowsNullPointerException() {
        Path mockPath = mock(Path.class);
        when(mockPath.toFile()).thenReturn(new File("dummy.csv"));
        assertThrows(NullPointerException.class, () -> csvFormat.print(mockPath, null));
    }
}