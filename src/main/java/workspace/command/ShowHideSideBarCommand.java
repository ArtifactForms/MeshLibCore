package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideSideBarCommand extends AbstractWorkspaceKeyCommand {

    public ShowHideSideBarCommand(WorkspaceModel model) {
        super(model);
        setName("Sidebar");
        setKey('y');
    }

    @Override
    public void execute() {
        getModel().setUiVisible(!getModel().isUiVisible());
    }

}
