package katlasik.board.dtos;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class NewAnswer {

    @NotNull
    private final long questionId;

    @NotNull
    @NotEmpty
    @Size(min = 20, max = 5000)
    private String content;

    public NewAnswer(@NotNull long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getQuestionId() {
        return questionId;
    }

    @Override
    public String toString() {
        return "NewAnswer{" +
                "questionId='" + questionId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewAnswer that = (NewAnswer) o;
        return Objects.equals(questionId, that.questionId) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, content);
    }
}

