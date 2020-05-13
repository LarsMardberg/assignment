package com.knightec.assignment.translation;


import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "translation")
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"language_code" , "from_word"}, name = "language_code_from_constraint")})
public class Translation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "language_code")
    @Pattern(regexp="^[a-zA-Z]{2}$",message = "languageCode didnt match [a-zA-Z]{2}")
    private String languageCode;

    @Column(name = "from_word")
    @NotNull
    private String from;

    @Column(name = "to_word")
    @NotNull
    private String to;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate;

    public Translation(String languageCode, String from, String to){
        this.languageCode = languageCode;
        this.from = from;
        this.to = to;
        this.createdDate = new Date();
    }
}
