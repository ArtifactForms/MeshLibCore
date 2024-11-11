package mesh.creator.beam;

import mesh.Mesh3D;

public class BeamCreator implements IBeamCreator {

	private float width;
	
	private float height;
	
	private float depth;
	
	private float thickness;
	
	private float taper;
	
	private ProfileType type;
	
	private IBeamCreator creator;

	public BeamCreator() {
		height = 0.85f;
		width = 0.5f;
		depth = 2.0f;
		thickness = 0.1f;
		taper = 0.0f;
		type = ProfileType.T_PROFILE;
	}

	private void initializeCreator() {
		creator = getCreatorByType();
	}

	private IBeamCreator getCreatorByType() {
		switch (type) {
		case O_PROFILE:
			return new BeamOProfileCreator();
		case U_PROFILE:
			return new BeamUProfileCreator();
		case C_PROFILE:
			return new BeamCProfileCreator();
		case L_PROFILE:
			return new BeamLProfileCreator();
		case I_PROFILE:
			return new BeamIProfileCreator();
		case T_PROFILE:
			return new BeamTProfileCreator();
		default:
			return new BeamTProfileCreator();
		}
	}

	private void setupCreator() {
		creator.setWidth(width);
		creator.setHeight(height);
		creator.setDepth(depth);
		creator.setThickness(thickness);
		creator.setTaper(taper);
	}

	public Mesh3D create() {
		initializeCreator();
		setupCreator();
		return creator.create();
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public float getTaper() {
		return taper;
	}

	public void setTaper(float taper) {
		this.taper = taper;
	}

	public ProfileType getType() {
		return type;
	}

	public void setType(ProfileType type) {
		this.type = type;
	}

}
