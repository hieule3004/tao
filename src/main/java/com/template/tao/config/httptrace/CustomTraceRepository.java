package com.template.tao.config.httptrace;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class CustomTraceRepository extends InMemoryHttpTraceRepository {

  private final AtomicReference<HttpTrace> lastTrace = new AtomicReference<>();

  @Override
  public List<HttpTrace> findAll() {
    return Collections.singletonList(this.lastTrace.get());
  }

  @Override
  public void add(HttpTrace trace) {
    this.lastTrace.set(trace);
  }
}