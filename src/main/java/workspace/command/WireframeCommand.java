package workspace.command;

import workspace.WorkspaceModel;

public class WireframeCommand extends AbstractWorkspaceKeyCommand {

    public WireframeCommand(WorkspaceModel model) {
        super(model);
        setName("Wireframe");
        setKey('z');
    }

    @Override
    public void execute() {
        getModel().setWireframe(!getModel().isWireframe());
    }

}
