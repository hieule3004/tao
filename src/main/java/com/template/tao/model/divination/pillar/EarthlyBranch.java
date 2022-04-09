package com.template.tao.model.divination.pillar;

import com.template.tao.model.divination.energy.Form;
import com.template.tao.model.divination.element.Phase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EarthlyBranch {

  ZI("RAT", Form.YANG, Phase.SHUI),
  CHOU("OX", Form.YIN, Phase.TU),
  YIN("TIGER", Form.YANG, Phase.MU),
  MAO("RABBIT", Form.YIN, Phase.MU),
  CHEN("DRAGON", Form.YANG, Phase.TU),
  SI("SNAKE", Form.YIN, Phase.HUO),
  WU("HORSE", Form.YANG, Phase.HUO),
  WEI("GOAT", Form.YIN, Phase.TU),
  SHEN("MONKEY", Form.YANG, Phase.JIN),
  YOU("ROOSTER", Form.YIN, Phase.JIN),
  XU("DOG", Form.YANG, Phase.TU),
  HAI("PIG", Form.YIN, Phase.SHUI),
  ;

  private final String zodiac;
  private final Form energy;
  private final Phase phase;

  //time associate
}
