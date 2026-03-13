package parse;

import java.util.List;

public interface FlagParseListener {

  void onFlagParsed(Flag flag);

  void onInvalidArguments(List<String> invalidArguments);
}
