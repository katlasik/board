package katlasik.board.dtos;

import java.util.Objects;

public class QuestionAdded {

    private final Long questionId;
    private final String originalPosterName;
    private final String anwserPosterName;

    public QuestionAdded(Long questionId, String originalPosterName, String anwserPosterName) {
        this.questionId = questionId;
        this.originalPosterName = originalPosterName;
        this.anwserPosterName = anwserPosterName;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getOriginalPosterName() {
        return originalPosterName;
    }

    public String getAnwserPosterName() {
        return anwserPosterName;
    }

    @Override
    public String toString() {
        return "QuestionAdded{" +
                "questionId='" + questionId + '\'' +
                ", originalPosterName='" + originalPosterName + '\'' +
                ", anwserPosterName='" + anwserPosterName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAdded that = (QuestionAdded) o;
        return Objects.equals(questionId, that.questionId) &&
                Objects.equals(originalPosterName, that.originalPosterName) &&
                Objects.equals(anwserPosterName, that.anwserPosterName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, originalPosterName, anwserPosterName);
    }
}
