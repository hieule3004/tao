package com.template.tao.config;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.template.tao.model.divination.bookofchanges.BookOfChanges;
import com.template.tao.model.divination.bookofchanges.DivinationResult;
import com.template.tao.model.divination.bookofchanges.Hexagram;
import com.template.tao.model.divination.bookofchanges.Interpretation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphqlConfig {

  @Bean
  public BookOfChanges bookOfChanges() {
    return new BookOfChanges();
  }

  @Bean
  public Query query(BookOfChanges bookOfChanges) {
    return new Query(bookOfChanges);
  }

  @RequiredArgsConstructor
  private static class Query implements GraphQLQueryResolver {

    private final BookOfChanges bookOfChanges;

    public DivinationResult cast(String question, Interpretation interpretation) {
      return this.bookOfChanges.cast(question, interpretation);
    }

    public DivinationResult lookupResult(String question, Interpretation interpretation, String resultString) {
      return this.bookOfChanges.lookupResult(question, interpretation, resultString);
    }

    public Hexagram lookupHexagram(int sequenceIndex) {
      return this.bookOfChanges.lookupHexagram(sequenceIndex);
    }
  }
}
