package com.github.antksk.cleancode.bowling;

import java.util.function.Function;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MyGame implements Game {

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
  static final int         PERFACT_CLEAR            = 10;

  @Slf4j
  static class Rolling {
    private final int[] rolls       = new int[BOWLING_GAME_MAX_COUNT + BONUS];
    private int         currentRoll = 0;

    public Rolling() {
      currentRoll = 0;
    }

    public int getCurrentRoll() {
      return currentRoll;
    }

    public void iterateRoll(int pins) {
      rolls[currentRoll++] = pins;
    }
    
    private boolean isPerfactClear( int pins ){
      return PERFACT_CLEAR == pins; 
    }
    
    public boolean isStrike(int frame){
      return isPerfactClear( firstBollByFrame(frame) );
    }

    public boolean isSpare(int frame) {
      return isPerfactClear(firstBollByFrame(frame) + secondBollByFrame(frame));
    }
    // FP 적용
    public Integer ifSpare(int frame, Function<Integer, Integer> clearSpare, Function<Integer, Integer> failSpare ){
      return isSpare(frame) ?
        clearSpare.apply(frame) :
        failSpare.apply(frame)
      ;
    }

    // 현재 frame의 첫번째 볼
    public int firstBollByFrame(int frame) {
      return rolls[frame * GAME_COUNT_BY_STEP];
    }

    // 현재 frame의 두번째 볼
    public int secondBollByFrame(int frame) {
      return rolls[frame * GAME_COUNT_BY_STEP + 1];
    }
    // 현재 frame의 다음 frame의 첫 볼
    public int nextFrameFirstBoll(int frame){
      return rolls[frame * GAME_COUNT_BY_STEP + 2];
    }
  }

  Rolling rolling;

  public MyGame() {
    rolling = new Rolling();
  }

  /* (non-Javadoc)
   * @see com.github.antksk.cleancode.bowling.Game#roll(int)
   */
  @Override
  public void roll(int pins) {
    rolling.iterateRoll(pins);
  }

  /* (non-Javadoc)
   * @see com.github.antksk.cleancode.bowling.Game#score()
   */
  @Override
  public int score() {
    log.debug("rolls : {}", rolling.rolls);
    Function<Integer, Integer> strike = (frame) -> PERFACT_CLEAR + ((rolling.secondBollByFrame(frame) * 2) + rolling.nextFrameFirstBoll(frame));
    Function<Integer, Integer> clearSpare = (frame) -> PERFACT_CLEAR + rolling.nextFrameFirstBoll(frame);
    Function<Integer, Integer> failSpare = (frame) -> rolling.firstBollByFrame(frame) + rolling.secondBollByFrame(frame);
    
    return IntStream
        .range(GAME_START_BASE, BOWLING_GAME_FRAME_COUNT)
        .reduce(0, (score,frame)-> score += rolling.isStrike(frame) ? strike.apply(frame) : rolling.ifSpare(frame, clearSpare, failSpare) );
  }
}
