package poja.compute.only.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import poja.compute.only.PojaGenerated;

@PojaGenerated
public class BucketConf {

  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("aws.s3.bucket", () -> "dummy-bucket");
  }
}
