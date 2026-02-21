package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideXAxisCommand extends AbstractWorkspaceKeyCommand {

	public ShowHideXAxisCommand(WorkspaceModel model) {
		super(model);
		setName("X-Axis");
		setKey('1');
	}

	@Override
	public void execute() {
		getModel().setxAxisVisible(!getModel().isxAxisVisible());
	}

}
