package com.knightec.assignment.translation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description="A translation ")
public class TranslationDTO {

    @Pattern(regexp="^[a-zA-Z]{2}$",message = "languageCode didnt match [a-zA-Z]{2}")
    @ApiModelProperty(notes="A two letter language code description")
    @NotNull(message = "language code is mandatory")
    @NotBlank(message = "language code cannot be empty")
    private String languageCode;

    @NotNull(message = "from is mandatory")
    @NotBlank(message = "from cannot be empty")
    @ApiModelProperty(notes="The word to translate")
    private String from;

    @NotNull(message = "to is mandatory")
    @NotBlank(message = "to cannot be empty")
    @ApiModelProperty(notes="The translated word")
    private String to;

}