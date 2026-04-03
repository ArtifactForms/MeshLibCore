package server.modules.whitelist;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WhitelistService {

  private final Set<UUID> whitelisted = new HashSet<>();

  public void add(UUID id) {
    whitelisted.add(id);
  }

  public void remove(UUID id) {
    whitelisted.remove(id);
  }

  public boolean isWhitelisted(UUID id) {
    return whitelisted.contains(id);
  }

  public Set<UUID> getEntries() {
    return Collections.unmodifiableSet(whitelisted);
  }
}
