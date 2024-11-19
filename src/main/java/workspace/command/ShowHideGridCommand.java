package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideGridCommand extends AbstractWorkspaceKeyCommand {

    public ShowHideGridCommand(WorkspaceModel model) {
        super(model);
        setName("Grid");
        setKey('g');
    }

    @Override
    public void execute() {
        getModel().setGridVisible(!getModel().isGridVisible());
    }

}
