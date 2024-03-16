package org.interview.dto;

import lombok.Data;

@Data
public class ErrorMessageDTO {

  String message;

  public ErrorMessageDTO(String message) {
    this.message = message;
  }
}
