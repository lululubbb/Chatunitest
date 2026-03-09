package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class SerializationBenchmark_633_6Test {

  @Test
    @Timeout(8000)
  void main_invokesNonUploadingCaliperRunnerRun() {
    try (MockedStatic<NonUploadingCaliperRunner> mockedRunner = mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};
      SerializationBenchmark.main(args);
      mockedRunner.verify(() -> NonUploadingCaliperRunner.run(eq(SerializationBenchmark.class), eq(args)));
    }
  }
}