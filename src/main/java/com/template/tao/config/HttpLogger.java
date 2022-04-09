package com.template.tao.config;

import com.template.tao.utils.JsonUtils;
import com.template.tao.utils.SeqUtils;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class HttpLogger extends OncePerRequestFilter {

  private static final List<String> MINIMAL_MATCHERS = List.of(
      "[^\\.]+\\.\\w+", "/", "/graphiql", "/subscriptions"
  );

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper cachedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);
    filterChain.doFilter(cachedRequest, cachedResponse);
    this.doLog(cachedRequest, cachedResponse);
    cachedResponse.copyBodyToResponse();
  }

  private void doLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) throws IOException {
    if (MINIMAL_MATCHERS.find(request.getRequestURI()::matches).isDefined()) {
      log.info("{} {} {}", response.getStatus(), request.getMethod(), request.getRequestURI());
      return;
    }

    // todo: objectify
    log.info("\nRequest-Info: {} {}\nResponse-Status: {}\nRequest-Header: {}\nRequest-{}: {}\nResponse-Header: {}\nResponse-Body: {}",
        request.getMethod(), request.getRequestURI(),
        HttpStatus.resolve(response.getStatus()),
        JsonUtils.MAPPER.writeValueAsString(SeqUtils.stream(request.getHeaderNames())
            .toMap(k -> k, k -> SeqUtils.stream(request.getHeaders(k)).toList())),
        HttpMethod.GET.name().equals(request.getMethod()) ? "Parameter" : "Body",
        HttpMethod.GET.name().equals(request.getMethod())
            ? JsonUtils.MAPPER.writeValueAsString(HashMap.ofAll(request.getParameterMap()))
            : new String(request.getContentAsByteArray(), request.getCharacterEncoding()),
        JsonUtils.MAPPER.writeValueAsString(List.ofAll(response.getHeaderNames())
            .distinct().toMap(k -> k, k -> List.ofAll(response.getHeaders(k)))),
        new String(response.getContentAsByteArray(), response.getCharacterEncoding()));
  }
}
