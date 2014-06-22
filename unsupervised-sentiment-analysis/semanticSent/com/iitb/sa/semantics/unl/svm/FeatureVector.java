package com.iitb.sa.semantics.unl.svm;

public class FeatureVector {
	private float pos;
	private float neg;
	private float obj;
	private float unlprediction;
	private float smileyprediction;

	public float getObj() {
		return obj;
	}

	public void setObj(float obj) {
		this.obj = obj;
	}

	public float getPos() {
		return pos;
	}

	public void setPos(float pos) {
		this.pos = pos;
	}

	public float getNeg() {
		return neg;
	}

	public void setNeg(float neg) {
		this.neg = neg;
	}

	public float getUnlprediction() {
		return unlprediction;
	}

	public void setUnlprediction(float unlprediction) {
		this.unlprediction = unlprediction;
	}

	public float getSmileyprediction() {
		return smileyprediction;
	}

	public void setSmileyprediction(float smileyprediction) {
		this.smileyprediction = smileyprediction;
	}

}
