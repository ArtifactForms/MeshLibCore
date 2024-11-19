package workspace.command;

import workspace.WorkspaceModel;

public class ShowHideVertexNormalsCommand extends AbstractWorkspaceKeyCommand {

    public ShowHideVertexNormalsCommand(WorkspaceModel model) {
        super(model);
        setName("Vertex Normals");
        setKey('v');
    }

    @Override
    public void execute() {
        getModel()
                .setVertexNormalsVisible(!getModel().isVertexNormalsVisible());
    }

}
