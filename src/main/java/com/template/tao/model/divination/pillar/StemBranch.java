package com.template.tao.model.divination.pillar;

import lombok.Data;
import lombok.ToString;

@ToString(includeFieldNames = false)
@Data(staticConstructor = "of")
public class StemBranch {

  private final HeavenlyStem stem;
  private final EarthlyBranch branch;

  public enum Type {

    YEAR, MONTH, DAY, HOUR
  }
}
