package engine.debug.core.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.debug.core.command.DebugCommand;
import engine.debug.core.config.DebugDepthMode;
import workspace.ui.Graphics;

public final class DebugRenderer {

    private final Map<Class<? extends DebugCommand>, DebugCommandRenderer<?>> renderers =
            new HashMap<>();

    public DebugRenderer() {
        register(new DebugLineRenderer());
        register(new DebugGridRenderer());
        register(new DebugSphereRenderer());
        register(new DebugPointRenderer());
        register(new DebugRayRenderer());
        register(new DebugAxisRenderer());
        register(new DebugBoundsRenderer());
    }

    public <T extends DebugCommand> void register(DebugCommandRenderer<T> renderer) {
        renderers.put(renderer.getCommandType(), renderer);
    }

    @SuppressWarnings("unchecked")
    public void render(Graphics g, List<DebugCommand> commands) {

        DebugDepthMode currentDepthMode = DebugDepthMode.DEPTH_TESTED;
        g.enableDepthTest();

        for (DebugCommand cmd : commands) {
            DebugCommandRenderer renderer = renderers.get(cmd.getClass());
            if (renderer == null) continue;

            if (cmd.depthMode != currentDepthMode) {
                applyDepthMode(g, cmd.depthMode);
                currentDepthMode = cmd.depthMode;
            }

            renderer.render(g, cmd);
        }

        g.enableDepthTest();
    }

    private void applyDepthMode(Graphics g, DebugDepthMode mode) {
        switch (mode) {
            case NO_DEPTH -> g.disableDepthTest();
            case DEPTH_TESTED -> g.enableDepthTest();
        }
    }
}
