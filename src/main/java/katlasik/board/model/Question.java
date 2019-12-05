package katlasik.board.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    private User user;

    @Size(min = 10, max = 500)
    @NotEmpty
    @NotNull
    private String title;

    @NotEmpty
    @NotNull
    @Size(min = 10, max = 5000)
    private String content;

    @OrderBy("createdOn")
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ElementCollection
    @CollectionTable(name = "question_images")
    private Set<Image> images = new HashSet<>();

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswers(Answer answer){
        this.answers.add(answer);
        answer.setQuestion(this);
    }

    public Set<Image> getImages() {
        return images;
    }

    public void addImage(Image image){
        images.add(image);
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", content='" + content +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id == question.id &&
                Objects.equals(title, question.title) &&
                Objects.equals(createdOn, question.createdOn) &&
                Objects.equals(content, question.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, createdOn);
    }

}
