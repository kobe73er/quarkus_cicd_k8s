package org.interview.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LookupResponseDTO {

  private List<IpAddressDTO> addresses;
  private String client_ip;
  private String domain;
  private long created_at;

  public static class LookupResponseDTOBuilder {

    public LookupResponseDTOBuilder addresses(List<String> addresses) {
      this.addresses = addresses.stream().map(IpAddressDTO::new).collect(Collectors.toList());
      return this;
    }
  }
}
