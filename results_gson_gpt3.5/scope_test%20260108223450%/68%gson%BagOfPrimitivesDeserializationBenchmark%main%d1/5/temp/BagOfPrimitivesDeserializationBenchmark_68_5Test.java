package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

import com.google.caliper.BeforeExperiment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class BagOfPrimitivesDeserializationBenchmark_68_5Test {

  @Test
    @Timeout(8000)
  void testMain_invokesNonUploadingCaliperRunnerRun() {
    try (MockedStatic<NonUploadingCaliperRunner> mockedRunner = Mockito.mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};
      BagOfPrimitivesDeserializationBenchmark.main(args);
      mockedRunner.verify(() -> NonUploadingCaliperRunner.run(BagOfPrimitivesDeserializationBenchmark.class, args));
    }
  }
}