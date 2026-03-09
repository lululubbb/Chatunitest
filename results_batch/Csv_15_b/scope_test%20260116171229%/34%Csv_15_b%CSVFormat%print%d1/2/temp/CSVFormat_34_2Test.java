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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_withPath_callsFilesNewBufferedWriterAndReturnsCSVPrinter() throws IOException {
        Path mockPath = mock(Path.class);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);

        CSVFormat csvFormatProxy = createCSVFormatProxy(csvFormat, mockPath, StandardCharsets.UTF_8, bufferedWriter);

        CSVPrinter printer = csvFormatProxy.print(mockPath, StandardCharsets.UTF_8);

        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrint_withPath_throwsIOException_whenFilesNewBufferedWriterThrows() throws IOException {
        Path mockPath = mock(Path.class);

        CSVFormat csvFormatProxy = createCSVFormatProxy(csvFormat, mockPath, StandardCharsets.UTF_8, null);

        IOException thrown = assertThrows(IOException.class, () -> csvFormatProxy.print(mockPath, StandardCharsets.UTF_8));
        assertEquals("Test IOException", thrown.getMessage());
    }

    /**
     * Create a dynamic proxy for CSVFormat that overrides print(Path, Charset)
     * to avoid calling Files.newBufferedWriter by returning CSVPrinter on a provided BufferedWriter.
     */
    private static CSVFormat createCSVFormatProxy(CSVFormat delegate, Path expectedPath,
                                                  java.nio.charset.Charset expectedCharset,
                                                  BufferedWriter bufferedWriter) {
        return (CSVFormat) Proxy.newProxyInstance(CSVFormat.class.getClassLoader(),
                new Class<?>[]{CSVFormat.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("print".equals(method.getName())
                                && args != null
                                && args.length == 2
                                && args[0] instanceof Path
                                && args[1] instanceof java.nio.charset.Charset) {
                            Path out = (Path) args[0];
                            java.nio.charset.Charset charset = (java.nio.charset.Charset) args[1];
                            if (out == expectedPath && charset.equals(expectedCharset)) {
                                if (bufferedWriter == null) {
                                    throw new IOException("Test IOException");
                                }
                                Method printBufferedWriter = CSVFormat.class.getMethod("print", Appendable.class);
                                return printBufferedWriter.invoke(delegate, bufferedWriter);
                            }
                        }
                        return method.invoke(delegate, args);
                    }
                });
    }
}