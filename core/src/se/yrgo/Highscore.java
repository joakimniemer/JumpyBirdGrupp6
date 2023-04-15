package se.yrgo;

public class Highscore implements Comparable<Highscore> {
    private String name;
    private int score;

    Highscore(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Highscore highscore) {
        return this.score - highscore.getScore();
    }
}
