package poja.compute.only.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import poja.compute.only.PojaGenerated;

@PojaGenerated
public class EmailConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.ses.source", () -> "dummy-ses-source");
  }
}
