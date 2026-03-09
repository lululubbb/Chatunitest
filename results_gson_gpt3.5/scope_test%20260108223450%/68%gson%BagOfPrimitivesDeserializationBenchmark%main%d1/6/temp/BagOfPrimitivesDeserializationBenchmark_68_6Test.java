package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class BagOfPrimitivesDeserializationBenchmark_68_6Test {

  @Test
    @Timeout(8000)
  void testMain_invokesNonUploadingCaliperRunnerRun() {
    try (MockedStatic<NonUploadingCaliperRunner> mockedRunner = mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};
      BagOfPrimitivesDeserializationBenchmark.main(args);
      mockedRunner.verify(() -> NonUploadingCaliperRunner.run(eq(BagOfPrimitivesDeserializationBenchmark.class), eq(args)));
    }
  }
}