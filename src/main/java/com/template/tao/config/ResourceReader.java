package com.template.tao.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class ResourceReader {

  private final ResourceLoader resourceLoader;

  public String readAsString(String location) {
    Resource resource = this.resourceLoader.getResource(location);
    try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
