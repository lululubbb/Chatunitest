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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

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
    void testPrint_File_Valid() throws IOException {
        File tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();

        CSVPrinter printer = csvFormat.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);
        // The OutputStreamWriter should be closed on printer.close()
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_File_IOException() throws Exception {
        // Use a directory as file to cause FileOutputStream to throw IOException
        File dir = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(dir.isDirectory());

        IOException thrown = assertThrows(IOException.class, () -> {
            csvFormat.print(dir, StandardCharsets.UTF_8);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrint_File_NullCharset() throws Exception {
        File tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            csvFormat.print(tempFile, null);
        });
        assertNotNull(thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrint_File_ReflectionVerifyFields() throws Exception {
        File tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();

        CSVPrinter printer = csvFormat.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);

        // Using reflection to verify the private 'out' field inside CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outObj = outField.get(printer);
        assertNotNull(outObj);
        // The 'out' field is a BufferedWriter wrapping the OutputStreamWriter
        // So traverse to find the OutputStreamWriter inside

        // Unwrap BufferedWriter -> Writer chain to find OutputStreamWriter
        Object writer = outObj;
        while (writer != null && !(writer instanceof OutputStreamWriter)) {
            Field f;
            try {
                f = writer.getClass().getDeclaredField("out");
            } catch (NoSuchFieldException e) {
                break;
            }
            f.setAccessible(true);
            writer = f.get(writer);
        }
        assertNotNull(writer);
        assertTrue(writer instanceof OutputStreamWriter);

        OutputStreamWriter outputStreamWriter = (OutputStreamWriter) writer;
        assertEquals(OutputStreamWriter.class, outputStreamWriter.getClass());

        printer.close();
    }
}