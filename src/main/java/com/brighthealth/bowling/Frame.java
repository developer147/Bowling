package com.brighthealth.bowling;

public class Frame {
	private FrameNumber frameNumber;
	private Roll roll1;
	private Roll roll2;
	private int score = 0;
	private boolean isScoreFinal = false;
	
	public Frame(FrameNumber frameNumber, Roll roll1, Roll roll2) {
		this.frameNumber = frameNumber;
		this.roll1 = roll1;
		this.roll2 = roll2; 
	}
	
	public Roll getRoll1()  {
		return roll1;
	}
	
	public Roll getRoll2() {
		return roll2;
	}
	
	public FrameNumber getFrameNumber() {
		return frameNumber;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScoreFinal(boolean isScoreFinal) {
		this.isScoreFinal = isScoreFinal;
	}
	
	@Override
	public String toString() {
		return "Frame [frameNumber=" + frameNumber + ", roll1=" + roll1 + ", roll2=" + roll2 + ", score=" + score
				+ ", isScoreFinal=" + isScoreFinal + "]";
	}	
}
