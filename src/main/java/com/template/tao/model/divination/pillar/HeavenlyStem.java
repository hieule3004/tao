package com.template.tao.model.divination.pillar;

import com.template.tao.model.divination.energy.Form;
import com.template.tao.model.divination.element.Phase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeavenlyStem {

  JIA(Form.YANG, Phase.MU),
  YI(Form.YIN, Phase.MU),
  BING(Form.YANG, Phase.HUO),
  DING(Form.YIN, Phase.HUO),
  WU(Form.YANG, Phase.TU),
  JI(Form.YIN, Phase.TU),
  GENG(Form.YANG, Phase.JIN),
  XIN(Form.YIN, Phase.JIN),
  REN(Form.YANG, Phase.SHUI),
  GUI(Form.YIN, Phase.SHUI),
  ;

  private final Form energy;
  private final Phase phase;
}
