package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class NotAllProcessosSavedException extends RuntimeException {

  private final List<ProcessDTO> success;
  private final List<ProcessDTO> failed;
}
