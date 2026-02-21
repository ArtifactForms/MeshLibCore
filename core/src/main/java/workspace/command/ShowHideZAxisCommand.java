package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideZAxisCommand extends AbstractWorkspaceKeyCommand {

	public ShowHideZAxisCommand(WorkspaceModel model) {
		super(model);
		setName("Z-Axis");
		setKey('3');
	}

	@Override
	public void execute() {
		getModel().setzAxisVisible(!getModel().iszAxisVisible());
	}

}
