package mesh.conway;

import java.util.ArrayList;
import java.util.List;

import mesh.Mesh3D;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.FitToAABBModifier;

public class ConwayVariationSystem {

    private boolean fitToBounds = true;

    private int currentIndex;

    private int k;

    private String[] operations = { "a", "k", "d" };

    private List<String> expressions;

    public ConwayVariationSystem(int k) {
	this.k = k;
	expressions = createExpressions();
    }

    public String getCurrentExpression() {
	return expressions.get(currentIndex);
    }

    public boolean hasNext() {
	return currentIndex < expressions.size() - 1;
    }

    public int getExpressionCount() {
	return expressions.size();
    }

    public int getCurrentIndex() {
	return currentIndex;
    }

    public Mesh3D getNext() {
	Mesh3D seed = new CubeCreator().create();
	Conway conway = new Conway();
	conway.create(seed, expressions.get(currentIndex));

	if (fitToBounds)
	    new FitToAABBModifier(2, 2, 2).modify(seed);

	currentIndex++;
	return seed;
    }

    private List<String> createExpressions() {
	List<String> result = new ArrayList<String>();
	List<String> toAdd = new ArrayList<String>();

	for (String s : operations) {
	    result.add(s);
	}

	for (int j = 0; j < k - 1; j++) {
	    for (String s : result) {
		for (int i = 0; i < operations.length; i++) {
		    String r = new String(s + operations[i]);
		    toAdd.add(r);
		}
	    }
	    result.clear();
	    result.addAll(toAdd);
	    toAdd.clear();
	}

//		int expected = (int) Math.pow(operations.length, k);
//		System.out.println("expected:" + expected);
//		System.out.println("size: " + result.size());
//		for (String s : result) {
//			System.out.println(s);
//		}
	return result;
    }

    public boolean isFitToBounds() {
	return fitToBounds;
    }

    public void setFitToBounds(boolean fitToBounds) {
	this.fitToBounds = fitToBounds;
    }

}
