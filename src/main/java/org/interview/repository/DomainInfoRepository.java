package org.interview.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import org.interview.entity.DomainInfo;

@ApplicationScoped
public class DomainInfoRepository implements PanacheRepository<DomainInfo> {

  public List<DomainInfo> findLatest20Queries() {
    return find("ORDER BY createdAt DESC").page(0, 20).list();
  }
}
