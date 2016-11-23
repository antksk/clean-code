package com.github.antksk.cleancode.bowling;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Seung Gyum Kim
 * @see https://github.com/msbaek/bowling-game
 * @comment
 * 규칙
 *  - 볼링 게임은 10개의 프레임으로 구성된다.
 *  - 각 프레임은 대개 2 롤을 갖는다(10개의 핀을 쓰러 뜨리기 위해 2번의 기회를 갖는다).
 *  - Spare: 10 + next first roll에서 쓰러 뜨린 핀수.
 *  - Strike: 10 + next two rolls에서 쓰러 뜨린 핀수.
 *  - 10th 프레임은 특별. spare 처리하면 3번 던질 수 있음.
 */
@Slf4j
public class 볼링_게임_테스트 {

  Game game;

  @Before
  public void setUp() {
    game = new MyOriginalPurposeGame();
  }

  @Test
  public void canRoll() {
    game.roll(0);
  }

  @Test
  public void gutterGame() {
    // 총 게임 횟수 동안 한번두 pin를 쓰러 트린 적이 없다면?
    rollAll(0);
    // 모든 게임에 쓰러 트린 pin가 0이기 때문에 테스트 통과함
    assertThat(game.score(), is(0));
  }

  private void rollAll(int pin) {
    rollMany(MyStupidGame.BOWLING_GAME_MAX_COUNT, pin);
  }

  private void rollMany(int rollCount, final int pin) {

    IntStream.range(MyStupidGame.GAME_START_BASE, rollCount).forEach(i -> {
      // log.debug("{}", i);
      game.roll(pin);
    });
  }

  @Test
  public void allOnes() {
    rollAll(1);
    // 모든 게임에 쓰러 트린 pin가 0이기 때문에 테스트 통과함
    assertThat(game.score(), is(20));
  }

  // @Ignore
  @Test
  public void oneSpare() {
    game.roll(5); // 5개 쓰러트림
    game.roll(5); // 5+5 =10개 모두 쓰러트렸기 때문에 spare 없음
    // 보너스가 발생해서 10 + 다음 frame에 나는 점수를 더해줌
    game.roll(3); // 3개 쓰러트림
    game.roll(0); // 7개 spare 처리 하지 못함!!!!
    // 여기선, spare 처리를 하지 못했기 때문에, 기존 13( 10 + 현재 쓰러트린 3개 ) + 3 만 처리함

    // 나머지는 모두 쓰러트린 볼 없음
    rollMany(16, 0);
    // spare가 생겨서, 다음 roll에 +3이 될 거라고 예상엤는 데,
    assertThat(game.score(), is(16)); // 단순히 score에 pin 갯수를 더했기 때문에, 에러 발생!!!
  }
  
  /*
  @Test
  public void oneStrike() {
      // frame 1
      game.roll(10); // 첫 fraem에 strike가 나면
      // 10 + 같은 frame의 두번째 볼 점수 + 다음 프레임 첫번째 점수를 더함
      game.roll(5); // 사실상 두번째 roll은 첫번째가 strike 이기 때문에 2배가 됨
      
      // fraem 2
      game.roll(3);
      game.roll(0);
      
      // 첫번째 프레임 첫번째 공에 strike 발생으로,
      // 10 + 5 + 다음 프레임 첫번째 공(3점)으로 
      // frame 1 : 18점
      // fraem 2 :  3점
      rollMany(15, 0);
      assertThat(game.score(), is(26));
  }
  @Test
  public void perfectGame() {
      rollMany(12, 10);
      assertThat(game.score(), is(300));
  }
  */
}
