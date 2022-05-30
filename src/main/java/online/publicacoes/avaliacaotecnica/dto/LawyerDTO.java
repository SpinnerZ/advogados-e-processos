package online.publicacoes.avaliacaotecnica.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class LawyerDTO {

  @NotBlank(message = "Lawyer username cannot be blank")
  private final String username;

  private Set<ProcessDTO> processes = new HashSet<>();
}
