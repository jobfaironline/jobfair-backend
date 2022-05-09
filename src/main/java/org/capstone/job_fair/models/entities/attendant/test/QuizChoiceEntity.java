package org.capstone.job_fair.models.entities.attendant.test;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz_choice", schema = "dbo")
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
    @JoinColumn(name = "quiz_question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private QuizQuestionEntity quizQuestion;

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
