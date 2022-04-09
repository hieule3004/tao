package com.template.tao.model.divination.bookofchanges;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DivinationResult {

  private final String question;
  private final String value;
  private final Hexagram current;
  private final List<Hexagram> future;
  private final List<Integer> movingLines;
  private final String interpretation;
}
