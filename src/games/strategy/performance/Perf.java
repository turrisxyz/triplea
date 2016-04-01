package games.strategy.performance;

import java.util.prefs.Preferences;


/**
 * Provides a high level API to the game engine for performance measurements.
 * This class handles the library details and sends output to 'PerformanceConsole.java'
 */
public class Perf {

  private static final String LOG_PERFORMANCE_KEY = "logPerformance";
  private static boolean enabled;

  static {
    enabled = isEnabled();
    if (enabled) {
      PerformanceConsole.getInstance().setVisible(true);
    }
  }

  public static void setEnabled(final boolean isEnabled) {
    if (enabled != isEnabled) {
      enabled = isEnabled;
      PerformanceConsole.getInstance().setVisible(enabled);
      storeEnabledPreference();
    }
  }

  private static void storeEnabledPreference() {
    final Preferences prefs = Preferences.userNodeForPackage(EnablePerformanceLoggingCheckBox.class);
    prefs.put(LOG_PERFORMANCE_KEY, Boolean.valueOf(enabled).toString());
  }

  public static boolean isEnabled() {
    final Preferences prefs = Preferences.userNodeForPackage(EnablePerformanceLoggingCheckBox.class);
    return prefs.getBoolean(LOG_PERFORMANCE_KEY, false);
  }

  public static PerfTimer startTimer(final String title) {
    if (!enabled) {
      return PerfTimer.DISABLED_TIMER;
    } else {
      return new PerfTimer(title);
    }
  }

  protected static void processResult(final long stopNanos, final PerfTimer perfTimer) {
    final long stopMicros = stopNanos / 1000;

    final long milliFraction = (stopMicros % 1000) / 100;
    final long millis = (stopMicros / 1000);
    PerformanceConsole.getInstance().append(millis + "." + milliFraction + " ms - " + perfTimer.title + "\n");
  }
}
