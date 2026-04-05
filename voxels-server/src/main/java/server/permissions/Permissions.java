package server.permissions;

public class Permissions {

  public static final String BLOCK_BREAK = "block.break";

  public static final String BLOCK_PLACE = "block.place";

  public static final String BLOCK_PICK = "block.pick";

  public static final String ITEM_DROP = "item.drop";

  public static final String ADMIN_BYPASS = "admin.bypass";

  public static final String ADMIN_DEBUG = "admin.debug";

  public static final String CHAT = "chat";

  // -------------------------------------
  // COMMAND PERMISSIONS
  // -------------------------------------

  public static final String COMMAND_OP = "command.admin.op";

  public static final String COMMAND_DEOP = "command.admin.deop";

  public static final String COMMANDS = "commands";

  public static final String COMMAND_STOP = "command.stop"; // FIXME Check duplicate

  public static final String COMMAND_ECHO = "command.echo";

  public static final String COMMAND_TELEPORT = "command.teleport";

  public static final String COMMAND_TELEPORT_SELF = "command.teleport.self";

  public static final String COMMAND_TELEPORT_OTHERS = "command.teleport.others";

  public static final String COMMAND_TOP = "command.top";

  public static final String COMMAND_WORLD_TIME = "command.world.time";

  public static final String COMMAND_WORLD_SEED = "command.world.seed";

  public static final String COMMAND_WORLD_SAVE = "command.world.save";

  public static final String COMMAND_KICK = "command.kick";

  public static final String COMMAND_PLAYERS = "command.players";

  public static final String COMMAND_WORLD_POSITION = "command.world.position";
  
  public static final String COMMAND_WORLD_POSITION_OTHERS = "command.world.position.others";

  public static final String COMMAND_SERVER_STOP = "command.server.stop";

  public static final String COMMAND_INVENTORY = "command.inventory";

  public static final String COMMAND_HELP = "command.help";

  public static final String COMMAND_GAMEMODE = "command.gamemode";

  public static final String COMMAND_CHUNK = "command.chunk";

  public static final String COMMAND_UUID = "command.uuid";

  public static final String COMMAND_BROADCAST = "command.broadcast";

  public static final String COMMAND_MSG = "command.msg";

  private Permissions() {
    // No instances
  }
}
