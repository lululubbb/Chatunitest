package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

import com.google.caliper.BeforeExperiment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BagOfPrimitivesDeserializationBenchmark_68_3Test {

  @Test
    @Timeout(8000)
  void testMain_invokesNonUploadingCaliperRunnerRun() {
    try (MockedStatic<NonUploadingCaliperRunner> mockedRunner = mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = {"arg1", "arg2"};
      BagOfPrimitivesDeserializationBenchmark.main(args);
      mockedRunner.verify(() -> NonUploadingCaliperRunner.run(BagOfPrimitivesDeserializationBenchmark.class, args));
    }
  }
}