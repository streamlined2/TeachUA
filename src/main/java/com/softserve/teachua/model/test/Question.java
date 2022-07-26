package com.softserve.teachua.model.test;

import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private QuestionCategory questionCategory;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "question")
    private Set<QuestionTest> questionTest = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "question")
    private Set<Answer> answers = new HashSet<>();
}
