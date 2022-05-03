package org.capstone.job_fair.models.entities.attendant.cv.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "quiz_choice", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizChoiceEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @Column(name = "content")
    private String content;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    @Column(name = "is_selected")
    private Boolean isSelected = false;
    @Column(name = "quiz_question_id")
    private String quizQuestionId;
    @Column(name = "origin_choice_id")
    private String originChoiceId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuizChoiceEntity that = (QuizChoiceEntity) o;
        return id != null && Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
