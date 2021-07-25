package com.brighthealth.bowling;

import java.util.Random;
import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BowlingApplication {

	public static void main(String[] args) {
		//SpringApplication.run(BowlingApplication.class, args);
		System.out.println("Hello Brighthealth!");
		
		Player player = getPlayerTest1();
		
		startPlay(player);
		
//		int totalPlayers = getTotalPlayers();
//		
//		Player[] players = initializePlayers(totalPlayers);
//		
//		startPlaying(players);
	}
	
	private static void startPlay(Player currentPlayer) {
		for (int currentFrameIndex = 1; currentFrameIndex <= 10; currentFrameIndex++) {
			FrameNumber currentFrameNumber = FrameNumber.getFrameNumber(currentFrameIndex);
			Frame currentFrame = currentPlayer.getFrame(FrameNumber.getFrameNumber(currentFrameIndex));
			Roll roll1 = currentFrame.getRoll1();
			
			FrameNumber previousFrameNumber = currentFrameIndex == 1 ? null : FrameNumber.getFrameNumber(currentFrameIndex - 1);
			Frame previousFrame = previousFrameNumber ==  null ? null : currentPlayer.getFrame(previousFrameNumber);
			
			FrameNumber lastToPreviousFrameNumber = currentFrameIndex <= 2 ? null : FrameNumber.getFrameNumber(currentFrameIndex - 2);
			Frame lastToPreviousFrame = lastToPreviousFrameNumber == null ? null : currentPlayer.getFrame(lastToPreviousFrameNumber);
			
			FrameNumber secondLastToPreviousFrameNumber = currentFrameIndex <= 3 ? null : FrameNumber.getFrameNumber(currentFrameIndex - 3);
			Frame secondLastToPreviousFrame = secondLastToPreviousFrameNumber == null ? null : currentPlayer.getFrame(secondLastToPreviousFrameNumber);
			
			// if roll1 is not a strike, check previous frame's second roll and
			// if it's a spare, set last frame's score.
			if (roll1 != Roll.STRIKE) {
				if (previousFrame != null) {
					// if roll1 is not a strike, check previous frame's second roll and
					// if it's a spare, set last frame's score.					
					if (previousFrame.getRoll2() == Roll.SPARE) {
						if (lastToPreviousFrame != null) {
							previousFrame.setScore(lastToPreviousFrame.getScore() + 10 + roll1.getScore());
						} else {
							previousFrame.setScore(10 + roll1.getScore());
						}
					// if roll1 is not a strike, and previous frame's first roll is a strike,
					// and last to previous frame's first roll is also a strike then 
					// update last to previous frame's score.
					} else if (previousFrame.getRoll1()  == Roll.STRIKE) {
						if (lastToPreviousFrame != null && lastToPreviousFrame.getRoll1() == Roll.STRIKE) {
							if (secondLastToPreviousFrame != null) {
								lastToPreviousFrame.setScore(secondLastToPreviousFrame.getScore() + 20 + roll1.getScore()); 
							} else {
								lastToPreviousFrame.setScore(20 + roll1.getScore());
							}
						}
					}
					previousFrame.setScoreFinal(true);
				}
			}
			
			if (roll1 == Roll.STRIKE) {
				if (previousFrame != null) {
					if (previousFrame.getRoll2() == Roll.SPARE) {
						if (lastToPreviousFrame != null) {
							previousFrame.setScore(lastToPreviousFrame.getScore() + 20);
						} else {
							previousFrame.setScore(20);
						}
					}
				}
			}
			
			Roll roll2 = currentFrame.getRoll2();
			if (roll2 != Roll.EMPTY) {
				// check if previous frame had a strike. If  yes, update previous frame score.
				if (previousFrame != null) {
					if (previousFrame.getRoll1() == Roll.STRIKE) {
						int roll2Score = roll2.getScore();
						// if roll2 is a spare, we can't use 99 but calculate the real value using roll1
						if (roll2 ==  Roll.SPARE) {
							roll2Score = 10 - roll1.getScore();
						}
						if (lastToPreviousFrame != null) {
							previousFrame.setScore(lastToPreviousFrame.getScore() + 10 + roll1.getScore() + roll2Score);
						} else {
							previousFrame.setScore(10 + roll1.getScore() + roll2Score);
						}
					}
				}
			}
			
			// When player's frame does not have a strike or a spare, update frame's score immediately.
			if (roll1 != Roll.STRIKE && roll2 != Roll.SPARE) {
				int currentFrameScore = roll1.getScore() + roll2.getScore();
				if (currentFrameIndex == 1) {
					currentFrame.setScore(currentFrameScore);
				} else {
					previousFrame = currentPlayer.getFrame(previousFrameNumber);
					currentFrame.setScore(previousFrame.getScore() + currentFrameScore);
				}
				currentFrame.setScoreFinal(true);
			}
			currentPlayer.setFrame(currentFrameNumber, currentFrame);
			//System.out.println(currentPlayer);
		}
		System.out.println(currentPlayer);
	}
	
	private static void startPlaying(Player[] players) {
		int totalPlayers = players.length;
		int max = 11;
		int min = 0;
		Random random = new Random();
		
		// 10 frames
		for (int currentFrameIndex = 1; currentFrameIndex <= 10; currentFrameIndex++) {
			FrameNumber currentFrameNumber = FrameNumber.getFrameNumber(currentFrameIndex);
			
			// each player gets to play the current frame
			for (int currentPlayerIndex = 0; currentPlayerIndex < totalPlayers; currentPlayerIndex++) {
				Player currentPlayer = players[currentPlayerIndex];
				
				int rollInt1 = random.nextInt(max - min) + min;
				int rollInt2 = -1;
				// If player gets a strike, skip roll2
				if (rollInt1 != 10) {
					int newMax = max - rollInt1;
					rollInt2 = random.nextInt(newMax - min) + min;
				}
				
				Roll roll1 = Roll.getRoll(rollInt1);
				FrameNumber previousFrameNumber = currentFrameIndex == 1 ? null : FrameNumber.getFrameNumber(currentFrameIndex - 1);
				Frame previousFrame = previousFrameNumber ==  null ? null : currentPlayer.getFrame(previousFrameNumber);
				
				FrameNumber lastToPreviousFrameNumber = currentFrameIndex <= 2 ? null : FrameNumber.getFrameNumber(currentFrameIndex - 2);
				Frame lastToPreviousFrame = lastToPreviousFrameNumber == null ? null : currentPlayer.getFrame(lastToPreviousFrameNumber);
				
				// if roll1 is not a strike, check previous frame's second roll and
				// if it's a spare, set last frame's score.
				if (roll1 != Roll.STRIKE) {
					if (currentFrameIndex != 1 && previousFrame.getRoll2() == Roll.SPARE) {
						if (lastToPreviousFrame != null) {
							previousFrame.setScore(lastToPreviousFrame.getScore() + 10 + rollInt1);
						} else {
							previousFrame.setScore(10 + rollInt1);
						}
						previousFrame.setScoreFinal(true);
					}
				}
				
				
				// 99 is an arbitrary number to mark a spare.
				Roll roll2 = rollInt1 + rollInt2 == 10 ? Roll.getRoll(99) : Roll.getRoll(rollInt2);
				
				//System.out.println("Player:" + currentPlayerIndex + "; Frame:" + currentFrameIndex + "; Roll1:" + roll1 + "; Roll2: " + roll2);
				Frame currentFrame = new Frame(currentFrameNumber, 
						roll1, 
						roll2);
				
				// When player's frame does not have a strike or a spare, update frame's score immediately.
				if (roll1 != Roll.STRIKE && roll2 != Roll.SPARE) {
					int currentFrameScore = rollInt1 + rollInt2;
					if (currentFrameIndex == 1) {
						currentFrame.setScore(currentFrameScore);
					} else {
						previousFrame = currentPlayer.getFrame(previousFrameNumber);
						currentFrame.setScore(previousFrame.getScore() + currentFrameScore);
					}
					currentFrame.setScoreFinal(true);
				}
				currentPlayer.setFrame(currentFrameNumber, currentFrame);
				System.out.println("Player:"+ currentPlayerIndex + "; Frame:" + currentFrame);
			}
		}		
	}	
	
	private static Player getPlayerTest1() {
		Player player = new  Player(0);
		
		FrameNumber frameNumber = FrameNumber.getFrameNumber(1);
		Frame frame1  = new Frame(frameNumber,
				Roll.getRoll(8),
				Roll.getRoll(0));
		player.setFrame(frameNumber, frame1);
		
		frameNumber = FrameNumber.getFrameNumber(2);
		Frame frame2  = new Frame(frameNumber,
				Roll.getRoll(5),
				Roll.getRoll(99));
		player.setFrame(frameNumber, frame2);
		
		frameNumber = FrameNumber.getFrameNumber(3);
		Frame frame3  = new Frame(frameNumber,
				Roll.getRoll(3),
				Roll.getRoll(5));
		player.setFrame(frameNumber, frame3);	
		
		frameNumber = FrameNumber.getFrameNumber(4);
		Frame frame4  = new Frame(frameNumber,
				Roll.getRoll(8),
				Roll.getRoll(1));
		player.setFrame(frameNumber, frame4);		
		
		frameNumber = FrameNumber.getFrameNumber(5);
		Frame frame5  = new Frame(frameNumber,
				Roll.getRoll(7),
				Roll.getRoll(1));
		player.setFrame(frameNumber, frame5);	
		
		frameNumber = FrameNumber.getFrameNumber(6);
		Frame frame6  = new Frame(frameNumber,
				Roll.getRoll(10),
				Roll.getRoll(-1));
		player.setFrame(frameNumber, frame6);	
		
		frameNumber = FrameNumber.getFrameNumber(7);
		Frame frame7  = new Frame(frameNumber,
				Roll.getRoll(9),
				Roll.getRoll(99));
		player.setFrame(frameNumber, frame7);	
		
		frameNumber = FrameNumber.getFrameNumber(8);
		Frame frame8  = new Frame(frameNumber,
				Roll.getRoll(10),
				Roll.getRoll(-1));
		player.setFrame(frameNumber, frame8);
		
		frameNumber = FrameNumber.getFrameNumber(9);
		Frame frame9  = new Frame(frameNumber,
				Roll.getRoll(10),
				Roll.getRoll(-1));
		player.setFrame(frameNumber, frame9);
		
		frameNumber = FrameNumber.getFrameNumber(10);
		Frame frame10  = new Frame(frameNumber,
				Roll.getRoll(8),
				Roll.getRoll(99));
		frame10.setRoll3(Roll.getRoll(6));
		player.setFrame(frameNumber, frame10);		
		
		
		//System.out.println(player);

		return player;
	}

	private static  int getTotalPlayers() {
		int totalPlayers = 0;
		System.out.println("Please enter number of players. It must be between 1 and 6.");
		while (true) {
			//Ask on the monitor for # of players
			Scanner sc = new Scanner(System.in);
		    System.out.print("Please print your number: ");
		    totalPlayers = sc.nextInt(); 
		    
		    if (totalPlayers < 1 || totalPlayers > 6) {
		    	//throw new RuntimeException("Invalid number of players. Please enter a value between 1 and 6.");
		    	System.out.println(totalPlayers + " is invalid number of players. Please enter a value between 1 and 6.");
		    } else {
		    	System.out.println("Your entered: " + totalPlayers);
		    	break;
		    }
		}
		return totalPlayers;
	}
	
	private static Player[] initializePlayers(int totalPlayers) {
		Player[] players = new Player[totalPlayers];
		//Initialize players
		for (int player = 0; player < totalPlayers; player++) {
			players[player] = new Player(player);
		}
		return players;
	}
}
