package org.interview.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnvironmentService {

  public boolean isRunningInKubernetes() {
    return System.getenv("KUBERNETES_SERVICE_HOST") != null;
  }
}
