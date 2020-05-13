package com.knightec.assignment.translation;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/v1")
public class TranslationController {

    @Autowired
    TranslationService translationService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("translation")
    @ApiOperation(value = "Find a translation")
    public ResponseEntity<TranslationDTO> getTranslationV1(
            @RequestParam
            @ApiParam(
                    name =  "from",
                    type = "String",
                    value = "The word to translate",
                    example = "Hello",
                    required = true)
            @NotNull(message = "from is mandatory")
            @NotBlank(message = "from cannot be empty") String from,
            @RequestParam
            @NotNull(message = "language code is mandatory")
            @NotBlank(message = "language code cannot be empty")
            @Pattern(regexp="^[a-zA-Z]{2}$",message = "languageCode didnt match [a-zA-Z]{2}")
            @ApiParam(
                    name =  "languageCode",
                    type = "String",
                    value = "A two letter language code description",
                    example = "se",
                    required = true)String languageCode){
            Optional<Translation> result = translationService.getTranslation(languageCode, from);
            if (result.isPresent()) {
                Translation foundTranslation = result.get();
                return ResponseEntity.ok(modelMapper.map(foundTranslation, TranslationDTO.class));
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Search didnt find any result");
            }
    }

    @PostMapping("translation")
    @ApiOperation(value = "Add a translation")
    public ResponseEntity<Void> postTranslationV1(@RequestBody @Valid TranslationDTO translationDTO){
        try {
            translationService.addTranslation(translationDTO.getLanguageCode(), translationDTO.getFrom(), translationDTO.getTo());
            return ResponseEntity.ok().build();
        } catch(IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
