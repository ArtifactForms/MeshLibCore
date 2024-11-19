package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideFaceNormalsCommand extends AbstractWorkspaceKeyCommand {

    public ShowHideFaceNormalsCommand(WorkspaceModel model) {
        super(model);
        setName("Face Normals");
        setKey('n');
    }

    @Override
    public void execute() {
        getModel().setFaceNormalsVisible(!getModel().isFaceNormalsVisible());
    }

}
