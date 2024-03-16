package org.interview.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.interview.dto.DomainHistoryDTO;
import org.interview.dto.IpAddressDTO;
import org.interview.dto.LookupResponseDTO;
import org.interview.entity.DomainInfo;
import org.interview.exception.DomainNotFoundException;
import org.interview.exception.InvalidDomainException;
import org.interview.repository.DomainInfoRepository;

@ApplicationScoped
public class DomainLookupService {

  @Inject DomainInfoRepository domainInfoRepository;

  public List<DomainHistoryDTO> getDomainHistory() {
    try {
      List<DomainInfo> domainInfos = domainInfoRepository.findLatest20Queries();

      return domainInfos.stream()
          .map(
              domainInfo -> {
                List<IpAddressDTO> ipAddresses =
                    domainInfo.getAddresses().stream()
                        .map(IpAddressDTO::new)
                        .collect(Collectors.toList());

                return new DomainHistoryDTO(
                    ipAddresses,
                    domainInfo.getClientIp(),
                    domainInfo.getDomain(),
                    domainInfo.getCreatedAt());
              })
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new BadRequestException("Invalid request");
    }
  }

  @Transactional
  public LookupResponseDTO lookup(String domain)
      throws DomainNotFoundException, InvalidDomainException {
    if (domain == null || domain.isEmpty()) {
      throw new InvalidDomainException("Domain cannot be empty");
    }
    // Regular expression for validating RFC 1035 compliant domain names
    if (!domain.matches("^(?!-)[A-Za-z0-9-]{1,63}(?<!-)(\\.[A-Za-z]{2,})+$")) {
      throw new InvalidDomainException("Domain format is invalid");
    }

    try {
      InetAddress[] addresses = InetAddress.getAllByName(domain);
      List<String> ipv4Addresses =
          Arrays.stream(addresses)
              .filter(addr -> addr instanceof Inet4Address) // 仅保留IPv4地址
              .map(InetAddress::getHostAddress)
              .collect(Collectors.toList());

      if (ipv4Addresses.isEmpty()) {
        throw new DomainNotFoundException("No IPv4 addresses found for domain");
      }

      DomainInfo domainInfo = domainInfoRepository.find("domain", domain).firstResult();
      boolean isNewRecord = domainInfo == null;

      if (isNewRecord) {
        domainInfo = new DomainInfo();
        domainInfo.setDomain(domain);
        domainInfo.setAddresses(ipv4Addresses);
        domainInfo.setClientIp(InetAddress.getLocalHost().getHostAddress());
        domainInfo.setCreatedAt(System.currentTimeMillis());
        domainInfoRepository.persist(domainInfo);
      } else {
        if (!domainInfo.getAddresses().equals(ipv4Addresses)
            || !domainInfo.getClientIp().equals(InetAddress.getLocalHost().getHostAddress())) {
          domainInfo.setAddresses(ipv4Addresses);
          domainInfo.setClientIp(InetAddress.getLocalHost().getHostAddress());
          domainInfo.setCreatedAt(System.currentTimeMillis());
          domainInfoRepository.persist(domainInfo);
        }
      }

      return LookupResponseDTO.builder()
          .addresses(ipv4Addresses)
          .client_ip(domainInfo.getClientIp())
          .domain(domain)
          .created_at(domainInfo.getCreatedAt())
          .build();

    } catch (UnknownHostException e) {
      throw new DomainNotFoundException("Domain not found: " + domain);
    } catch (IOException e) {
      throw new RuntimeException("Error retrieving client IP address", e);
    }
  }
}
