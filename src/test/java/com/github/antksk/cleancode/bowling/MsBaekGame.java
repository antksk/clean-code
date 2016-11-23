package com.github.antksk.cleancode.bowling;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MsBaekGame implements Game {
//볼링 게임의 frame 횟수
 private static final int BOWLING_GAME_FRAME_COUNT = 10;
 // 각 roll당 진행 되는 횟수
 static final int         GAME_COUNT_BY_STEP       = 2;
 // 전체 게임당 총 볼을 굴리는 횟수
 static final int         BOWLING_GAME_MAX_COUNT   = BOWLING_GAME_FRAME_COUNT * GAME_COUNT_BY_STEP;
 //
 // 10번째 스패어 까지 처리 하면, 1번 기회를 더 줌
 static final int         BONUS                    = 1;

 // 초기 게임 시작 값 게임 횟수( 게임 한 번당 총 2회 볼을 굴림 )
 static final int         GAME_START_BASE          = 0;
 
 // frame당 전체 boll를 쓰러트렸는 지 확인용
 static final int         PERFACT_CLEAR            = 10;
  private int[] rolls = new int[21];
  private int currentRoll = 0;

  @Override
  public void roll(int pins) {
      rolls[currentRoll++] = pins;
  }

  @Override
  public int score() {
    log.debug("rolls : {}", rolls);
      int score = 0;
      int firstFrame = 0;
      for(int frame = 0; frame < 10; frame++) {
        log.debug("firstFrame : {}", firstFrame);
          if(isStrike(firstFrame)) {
              score += 10 + nextTwoBallsForStrike(firstFrame);
              firstFrame += 1;
          }
          else if(isSpare(firstFrame)) {
              score += 10 + nextBallForSpare(firstFrame);
              firstFrame += 2;
          }
          else {
              score += nextBallsInFrame(firstFrame);
              firstFrame += 2;
          }
      }
      return score;
  }

  private int nextBallsInFrame(int firstFrame) {
      return rolls[firstFrame] + rolls[firstFrame + 1];
  }

  private int nextBallForSpare(int firstFrame) {
      return rolls[firstFrame + 2];
  }

  private int nextTwoBallsForStrike(int firstFrame) {
      return rolls[firstFrame + 1] + rolls[firstFrame + 2];
  }

  private boolean isStrike(int firstFrame) {
      return rolls[firstFrame] == 10;
  }

  private boolean isSpare(int firstFrame) {
      return rolls[firstFrame] + rolls[firstFrame + 1] == 10;
  }
}