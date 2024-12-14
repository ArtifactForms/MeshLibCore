package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideEdgesCommand extends AbstractWorkspaceKeyCommand {

	public ShowHideEdgesCommand(WorkspaceModel model) {
		super(model);
		setName("Edges");
		setKey('e');
	}

	@Override
	public void execute() {
		getModel().setEdgesVisible(!getModel().isEdgesVisible());
	}

}
