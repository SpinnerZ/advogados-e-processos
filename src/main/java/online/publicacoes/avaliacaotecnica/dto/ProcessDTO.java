package online.publicacoes.avaliacaotecnica.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import online.publicacoes.avaliacaotecnica.entities.Process;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class ProcessDTO {

  @NotNull @Positive private final Long number;
  private String defendant;
  private Boolean archived;
  @JsonIgnore private String attorneyUserName;

  public ProcessDTO(Process process) {

    this.number = process.getNumber();
    this.defendant = process.getDefendant();
    this.archived = process.getArchived();
    this.attorneyUserName = process.getAttorney().getUsername();
  }
}
