package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideYAxisCommand extends AbstractWorkspaceKeyCommand {

	public ShowHideYAxisCommand(WorkspaceModel model) {
		super(model);
		setName("Y-Axis");
		setKey('2');
	}

	@Override
	public void execute() {
		getModel().setyAxisVisible(!getModel().isyAxisVisible());
	}

}
