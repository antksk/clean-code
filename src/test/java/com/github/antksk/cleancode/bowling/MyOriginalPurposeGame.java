package com.github.antksk.cleancode.bowling;

import java.util.function.Function;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyOriginalPurposeGame implements Game {

  // 볼링 게임의 frame 횟수
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
  static final int         CLEAR            = 10;

  final int[]              rolls;
  int                      currentRoll;

  public MyOriginalPurposeGame() {
    rolls = new int[BOWLING_GAME_MAX_COUNT + BONUS];
    currentRoll = 0;
  }

  @Override
  public void roll(int pins) {
    rolls[currentRoll++] = pins;
  }

  @Override
  public int score() {
    log.debug("rolls : {} ", rolls);
    int firstFrame = 0;
    int score = 0;
    for( int frame = GAME_START_BASE; BOWLING_GAME_FRAME_COUNT > frame; ++frame ){
      if( isClearSpare(firstFrame) ){
        score += CLEAR + rolls[firstFrame+2];
        firstFrame+= 2;
      }else{
        score += rolls[firstFrame] + rolls[firstFrame+1];
        firstFrame+= 2;
      }
    }
    return score;
  }

  private boolean isClearSpare(int firstFrame) {
    return CLEAR == rolls[firstFrame] + rolls[firstFrame+1];
  }
}
