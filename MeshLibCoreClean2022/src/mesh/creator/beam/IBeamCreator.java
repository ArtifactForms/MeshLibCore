package mesh.creator.beam;

import mesh.creator.IMeshCreator;

public interface IBeamCreator extends IMeshCreator {

    float getWidth();

    void setWidth(float width);

    float getHeight();

    void setHeight(float height);

    float getDepth();

    void setDepth(float depth);

    float getThickness();

    void setThickness(float thickness);

    float getTaper();

    void setTaper(float taper);

}
