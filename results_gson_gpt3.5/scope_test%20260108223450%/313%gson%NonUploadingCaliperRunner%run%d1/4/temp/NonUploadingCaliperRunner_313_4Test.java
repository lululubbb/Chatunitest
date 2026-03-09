package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class NonUploadingCaliperRunner_313_4Test {

  @Test
    @Timeout(8000)
  void testRun_invokesCaliperMainWithConcatenatedArgs() {
    Class<?> testClass = String.class;
    String[] args = new String[] {"arg1", "arg2"};

    try (MockedStatic<CaliperMain> caliperMainMock = mockStatic(CaliperMain.class)) {
      NonUploadingCaliperRunner.run(testClass, args);

      String[] expectedArgs = new String[args.length + 1];
      expectedArgs[0] = "-Cresults.upload.options.url=";
      System.arraycopy(args, 0, expectedArgs, 1, args.length);

      caliperMainMock.verify(() -> CaliperMain.main(eq(testClass), eq(expectedArgs)));
    }
  }

  @Test
    @Timeout(8000)
  void testConcat_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "prefix";
    String[] others = new String[] {"a", "b"};

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    // Expected array length is others.length + 1
    assert result.length == others.length + 1;
    assert result[0].equals(first);
    for (int i = 0; i < others.length; i++) {
      assert result[i + 1].equals(others[i]);
    }

    // Test with empty others
    String[] emptyOthers = new String[0];
    String[] resultEmpty = (String[]) concatMethod.invoke(null, first, emptyOthers);
    assert resultEmpty.length == 1;
    assert resultEmpty[0].equals(first);
  }
}