package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class NotAllProcessosSavedException extends RuntimeException {

  private final Set<ProcessDTO> success;
  private final Set<ProcessDTO> failed;
}
