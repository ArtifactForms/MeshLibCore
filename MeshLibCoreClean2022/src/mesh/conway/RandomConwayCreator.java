package mesh.conway;

import math.Mathf;
import mesh.Mesh3D;
import mesh.creator.IMeshCreator;
import mesh.creator.primitives.CubeCreator;
import mesh.modifier.FitToAABBModifier;

public class RandomConwayCreator implements IMeshCreator {
	
	private int expressionLength;
	
	private boolean fitToBounds;
	
	private String lastExpression;
	
	public RandomConwayCreator() {
		fitToBounds = true;
		expressionLength = 5;
	}
	
	@Override
	public Mesh3D create() {
		String expression = createExpression();
		setLastExpression(expression);
		
		Mesh3D seed = new CubeCreator().create();
		Conway conway = new Conway();
		conway.create(seed, expression);
		
		if (fitToBounds)
			new FitToAABBModifier(2, 2, 2).modify(seed);
		
		return seed;
	}

	private String createExpression() {
		String expression = "";
		
		for (int i = 0; i < expressionLength; i++) {
			int random = Mathf.random(0, 3);
			
			switch (random) {
			case 0:
				expression += "k";
				break;
			case 1:
				expression += "a";
				break;
			case 2:
				expression += "d";
				break;
			default:
				break;
			}
		}
		
		return expression;
	}

	private void setLastExpression(String lastExpression) {
		this.lastExpression = lastExpression;
	}
	
	public String getLastExpression() {
		return lastExpression;
	}
	
	public int getN() {
		return expressionLength;
	}

	public void setN(int n) {
		this.expressionLength = n;
	}
	
}
