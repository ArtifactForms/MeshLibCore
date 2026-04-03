package server.events.events;

import java.util.UUID;

import server.commands.Command;
import server.events.CancellableEvent;

/**
 * Event fired before a command is executed.
 *
 * <p>This event is triggered after a command has been parsed, but before its execution logic is
 * run.
 *
 * <p>This event is cancellable. If cancelled, the command will not be executed.
 *
 * <p>Listeners may:
 *
 * <ul>
 *   <li>Cancel command execution
 *   <li>Provide a custom cancel reason (e.g. error message to the executor)
 *   <li>Inspect or log the raw command input
 * </ul>
 *
 * <p>This event is typically used for:
 *
 * <ul>
 *   <li>Permission checks
 *   <li>Command restrictions (cooldowns, disabled commands)
 *   <li>Logging or auditing command usage
 *   <li>Custom validation before execution
 * </ul>
 *
 * <p>The executor may be a player or the console. If the executor is {@code null}, the command was
 * executed by the console.
 *
 * <p>Note: The command has already been resolved to a {@link Command} instance, but has not yet
 * been executed.
 */
public class CommandPreExecuteEvent extends CancellableEvent {

  /** The UUID of the command executor, or {@code null} if executed by console. */
  private final UUID executor;

  /** The command that is about to be executed. */
  private final Command command;

  /**
   * The raw command input as entered by the executor (e.g. "/kick Steve Spamming").
   *
   * <p>This includes the command name and all arguments in their original form.
   */
  private final String rawInput;

  /**
   * The reason why execution was cancelled.
   *
   * <p>This message may be shown to the executor if the command is blocked.
   */
  private String cancelReason;

  /**
   * Creates a new {@code CommandPreExecuteEvent}.
   *
   * @param executor The UUID of the executor, or {@code null} if console.
   * @param command The resolved command instance.
   * @param rawInput The raw command input string.
   */
  public CommandPreExecuteEvent(UUID executor, Command command, String rawInput) {
    this.executor = executor;
    this.command = command;
    this.rawInput = rawInput;
  }

  /**
   * Returns whether the command was executed by the console.
   *
   * @return {@code true} if executed by console, {@code false} otherwise.
   */
  public boolean isConsole() {
    return executor == null;
  }

  /**
   * Returns the UUID of the executor.
   *
   * @return The executor UUID, or {@code null} if console.
   */
  public UUID getExecutor() {
    return executor;
  }

  /**
   * Returns the command that is about to be executed.
   *
   * @return The command instance.
   */
  public Command getCommand() {
    return command;
  }

  /**
   * Returns the raw command input.
   *
   * @return The full input string as entered by the executor.
   */
  public String getRawInput() {
    return rawInput;
  }

  /**
   * Returns the reason for cancellation.
   *
   * @return The cancel reason, or {@code null} if none is set.
   */
  public String getCancelReason() {
    return cancelReason;
  }

  /**
   * Sets the reason for cancelling the command execution.
   *
   * <p>This message can be shown to the executor as feedback.
   *
   * @param cancelReason The cancel reason.
   */
  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }
}
