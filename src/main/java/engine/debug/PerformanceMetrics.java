package engine.debug;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class PerformanceMetrics {

  private final OperatingSystemMXBean osBean;

  public PerformanceMetrics() {
    osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  }

  public double getCpuUsage() {
    return Math.round(osBean.getCpuLoad() * 100);
  }
}
