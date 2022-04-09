package com.template.tao.utils;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionUtils {

  public static final Connection SESSION = Jsoup.newSession()
      .ignoreContentType(true);

  public static Try<Document> get(String url) {
    return Try.ofCallable(SESSION.url(url)::get);
  }
}
