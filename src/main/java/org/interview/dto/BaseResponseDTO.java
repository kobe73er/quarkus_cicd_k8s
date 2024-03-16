package org.interview.dto;

import lombok.Data;

@Data
public class BaseResponseDTO {

  private String version;
  private long date;
  private boolean kubernetes;
}
