package edu.cnm.deepdive.craps.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.random.RandomGenerator;

public class Round {

  private State state = State.COME_OUT;
  private final List<Roll> rolls = new LinkedList<>();


  public State play(RandomGenerator rng) {
    int point = 0;
    while (!state.isTerminal()) {
      Roll roll = new Roll(1 + rng.nextInt(6), 1 + rng.nextInt(6));
      state = state.next(point, roll.value());
      if (!state.isTerminal() && point == 0) {
        point = roll.value();
      }
      rolls.add(roll);
    }
    return state;
  }

  public State getState() {
    return state;
  }

  public List<Roll> getRolls() {
    return Collections.unmodifiableList(rolls);
  }

  public enum State {
    COME_OUT {
      @Override
      public boolean isTerminal() {
        return false;
      }

      @Override
      public State next(int point, int roll) {
        return switch (roll) {
          case 7, 11 -> WIN;
          case 2, 3, 12 -> LOSS;
          default -> POINT;
        };
      }
    },
    POINT {
      @Override
      public boolean isTerminal() {
        return false;
      }

      @Override
      public State next(int point, int roll) {
        return (roll == point)
            ? WIN : (roll == 7)
            ? LOSS
            : this;

      }

      ;
    },
    LOSS,
    WIN;


    public State next(int point, int roll) {
      if (isTerminal()) {
        throw new IllegalStateException();

      }
      ;
      return this;
    }

    public boolean isTerminal() {
      return true;
    }

    public static State initial() {
      return COME_OUT;
    }
  }

  public record Roll(int die1, int die2) {

    public int[] dice() {
      return new int[]{die1, die2};

    }

    public int value() {
      return die1 + die2;

    }

  }
}
