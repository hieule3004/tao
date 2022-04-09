package com.template.tao.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.List;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
public class OEISResultDTO {

  private Integer number;
  private List<Integer> data;
  private String name;
  private List<String> comment;
  private List<String> reference;
  private List<String> link;
  private List<String> example;
  private List<String> xref;
  private String keyword;
  private String offset;
  private String author;
  private List<String> ext;
  private Integer references;
  private Integer revision;
  private LocalDateTime time;
  private LocalDateTime created;

  @JsonIgnore
  public List<Integer> getData() {
    return data;
  }

  @JsonProperty("static/js/data")
  public String getDataAsString() {
    return this.data
        .map(String::valueOf)
        .toJavaStream()
        .collect(Collectors.joining(","));
  }

  public void setDataAsString(String data) {
    this.data = List.of(data.split(",")).map(Integer::parseInt);
  }
}
