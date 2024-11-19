package workspace.command;

import workspace.WorkspaceModel;

public abstract class AbstractWorkspaceKeyCommand extends AbstractKeyCommand {

    private WorkspaceModel model;

    public AbstractWorkspaceKeyCommand(WorkspaceModel model) {
        this.model = model;
    }

    public WorkspaceModel getModel() {
        return model;
    }

    public void setModel(WorkspaceModel model) {
        this.model = model;
    }

}
