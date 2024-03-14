package org.interview.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
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

    @Inject
    DomainInfoRepository domainInfoRepository;

    public List<DomainHistoryDTO> getDomainHistory() {
        try {
            List<DomainInfo> domainInfos = domainInfoRepository.findLatest20Queries();

            return domainInfos.stream().map(domainInfo -> {
                List<IpAddressDTO> ipAddresses = domainInfo.getAddresses().stream()
                    .map(IpAddressDTO::new)
                    .collect(Collectors.toList());

                return new DomainHistoryDTO(ipAddresses, domainInfo.getClientIp(),
                    domainInfo.getDomain(), domainInfo.getCreatedAt());
            }).collect(Collectors.toList());
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
        if (!domain.matches("[a-zA-Z0-9.-]+")) {
            throw new InvalidDomainException("Domain format is invalid");
        }

        try {
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            List<String> ipv4Addresses = Arrays.stream(addresses)
                .filter(addr -> addr.getAddress().length
                    == 4) // Only IPv4 addresses, which are 4 bytes long
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
                domainInfoRepository.persist(domainInfo); // Persist the new record
            } else {
                // Record exists, perhaps update the existing record
                domainInfo.setAddresses(ipv4Addresses);
                domainInfo.setClientIp(InetAddress.getLocalHost().getHostAddress());
                domainInfo.setCreatedAt(System.currentTimeMillis());

                // Merge the updated record
                domainInfoRepository.persist(domainInfo);
            }

            // Return the response object
            return LookupResponseDTO.builder()
                .addresses(ipv4Addresses) // The custom setter method will handle the transformation
                .client_ip(domainInfo.getClientIp())
                .domain(domain)
                .created_at(domainInfo.getCreatedAt())
                .build();


        } catch (UnknownHostException e) {
            throw new DomainNotFoundException("Domain not found: " + domain);
        }
    }
}