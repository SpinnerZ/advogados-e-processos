package online.publicacoes.avaliacaotecnica.exceptions.dto;

import lombok.Builder;
import lombok.Data;
import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;

import java.util.List;

@Data
@Builder
public class NotAllProcessesCreatedDTO {

  private final List<ProcessDTO> success;
  private final List<ProcessDTO> failure;
}
