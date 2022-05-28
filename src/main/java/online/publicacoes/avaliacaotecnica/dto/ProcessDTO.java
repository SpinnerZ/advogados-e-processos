package online.publicacoes.avaliacaotecnica.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class ProcessDTO {

  @NotNull @Positive private final Long number;
  private String defendant;
  private Boolean archived;
  private String lawyerUsername;
}
