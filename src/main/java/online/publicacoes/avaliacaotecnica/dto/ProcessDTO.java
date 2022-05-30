package online.publicacoes.avaliacaotecnica.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProcessDTO {

  @NotNull(message = "Process number cannot be null")
  @Positive(message = "Process number must be a positive number")
  @EqualsAndHashCode.Include
  private final Long number;

  private String defendant;
  private Boolean archived;
  private String lawyerUsername;
}
