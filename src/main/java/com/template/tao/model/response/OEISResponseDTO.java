package com.template.tao.model.response;

import io.vavr.collection.List;
import lombok.Data;

@Data
public class OEISResponseDTO {

  private String greeting;
  private String query;
  private Integer count;
  private Integer start;
  private List<OEISResultDTO> results;
}
