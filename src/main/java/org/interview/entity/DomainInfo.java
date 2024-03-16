package org.interview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "domain_info",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"domain"})})
public class DomainInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> addresses;

  @Column(name = "client_ip")
  private String clientIp;

  private String domain;

  @Column(name = "created_at")
  private long createdAt;
}
