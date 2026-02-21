package math.noise.map;

import java.awt.Color;

@FunctionalInterface
public interface HeightColorMapper {

  /** Maps a normalized height value [0,1] to a color. */
  Color map(float height);
}
