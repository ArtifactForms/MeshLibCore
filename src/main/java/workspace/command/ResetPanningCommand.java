package workspace.command;

import workspace.WorkspaceModel;

public class ResetPanningCommand extends AbstractWorkspaceKeyCommand {

	public ResetPanningCommand(WorkspaceModel model) {
		super(model);
		setName("Reset Panning");
		setKey('c');
	}

	@Override
	public void execute() {
		getModel().setPanningX(0);
		getModel().setPanningY(0);
	}

}
