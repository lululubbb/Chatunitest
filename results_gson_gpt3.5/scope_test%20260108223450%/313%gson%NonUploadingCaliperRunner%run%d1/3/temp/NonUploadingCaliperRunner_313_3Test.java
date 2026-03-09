package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.runner.CaliperMain;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;

class NonUploadingCaliperRunner_313_3Test {

  @Test
    @Timeout(8000)
  void run_invokesCaliperMainWithPrependedArg() {
    Class<?> testClass = String.class;
    String[] args = new String[] {"arg1", "arg2"};

    try (MockedStatic<CaliperMain> caliperMainMock = mockStatic(CaliperMain.class)) {
      NonUploadingCaliperRunner.run(testClass, args);

      caliperMainMock.verify(() -> CaliperMain.main(eq(testClass), argThat(arg -> {
        if (arg == null) return false;
        if (arg.length != args.length + 1) return false;
        if (!arg[0].equals("-Cresults.upload.options.url=")) return false;
        for (int i = 0; i < args.length; i++) {
          if (!arg[i + 1].equals(args[i])) return false;
        }
        return true;
      })));
    }
  }

  @Test
    @Timeout(8000)
  void concat_returnsArrayWithFirstAndOthers() throws Exception {
    var concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "firstArg";
    String[] others = new String[] {"one", "two"};

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assert result.length == others.length + 1;
    assert result[0].equals(first);
    for (int i = 0; i < others.length; i++) {
      assert result[i + 1].equals(others[i]);
    }
  }

  @Test
    @Timeout(8000)
  void concat_returnsArrayWithOnlyFirstIfNoOthers() throws Exception {
    var concatMethod = NonUploadingCaliperRunner.class.getDeclaredMethod("concat", String.class, String[].class);
    concatMethod.setAccessible(true);

    String first = "onlyFirst";
    String[] others = new String[0];

    String[] result = (String[]) concatMethod.invoke(null, first, others);

    assert result.length == 1;
    assert result[0].equals(first);
  }
}