package com.brighthealth.bowling;

public class Player {
	private int playerId;
	private Frame[] frames;
	
	public Player(int playerId) {
		this.playerId = playerId;
		frames = new Frame[10];
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public void setFrame(FrameNumber frameNumber, Frame frame) {
		frames[frameNumber.getValue()-1] = frame;
	}
	
	public Frame getFrame(FrameNumber frameNumber) {
		return frames[frameNumber.getValue()-1];
	}
}
