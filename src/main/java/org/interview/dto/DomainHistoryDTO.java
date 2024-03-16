package org.interview.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainHistoryDTO {

  public List<IpAddressDTO> addresses;
  public String client_ip;
  public String domain;
  public long created_at;
}
