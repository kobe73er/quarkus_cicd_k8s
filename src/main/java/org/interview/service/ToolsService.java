package org.interview.service;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ToolsService {

  public boolean isValidIP(String ip) {
    if (ip == null || ip.trim().isEmpty()) {
      return false;
    }
    return ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
  }
}
