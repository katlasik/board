package katlasik.board.dtos;


import java.util.Objects;

public class QuestionView {

    private final long id;
    private final String title;
    private final int answerCount;

    public QuestionView(long id, String title, int answerCount) {
        this.id = id;
        this.title = title;
        this.answerCount = answerCount;
    }

    public String getTitle() {
        return title;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionView that = (QuestionView) o;
        return answerCount == that.answerCount &&
                Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, answerCount);
    }

    @Override
    public String toString() {
        return "AnswerView{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", answerCount=" + answerCount +
                '}';
    }
}

