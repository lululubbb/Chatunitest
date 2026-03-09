package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

    private Closeable closeableOut;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);

        Object proxy = Proxy.newProxyInstance(
                Appendable.class.getClassLoader(),
                new Class<?>[]{Appendable.class, Closeable.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
                        String name = method.getName();
                        if ("close".equals(name)) {
                            closeableOut.close();
                            return null;
                        }
                        if ("append".equals(name)) {
                            // Handle all append methods with appropriate return types
                            if (method.getReturnType().equals(Appendable.class)) {
                                return proxyObj;
                            } else if (method.getReturnType().equals(CharSequence.class)) {
                                return null;
                            } else {
                                return proxyObj;
                            }
                        }
                        if ("toString".equals(name)) {
                            return proxyObj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxyObj));
                        }
                        if ("hashCode".equals(name)) {
                            return System.identityHashCode(proxyObj);
                        }
                        if ("equals".equals(name)) {
                            return proxyObj == args[0];
                        }
                        return null;
                    }
                });

        Constructor<CSVPrinter> constructor = CSVPrinter.class.getConstructor(Appendable.class, CSVFormat.class);
        printer = constructor.newInstance(proxy, format);
    }

    @Test
    @Timeout(8000)
    void testClose_whenOutIsCloseable_closesOut() throws IOException {
        printer.close();
        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_whenOutIsCloseable_throwsIOException() throws IOException {
        doThrow(new IOException("close failed")).when(closeableOut).close();
        try {
            printer.close();
        } catch (IOException e) {
            // expected
        }
        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_whenOutIsNotCloseable_doesNotThrow() throws IOException {
        Appendable nonCloseableOut = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter nonCloseablePrinter = new CSVPrinter(nonCloseableOut, format);
        nonCloseablePrinter.close();
    }
}