package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;

@RequiredArgsConstructor
@Getter
public class ProcessCreatedInsteadOfUpdatedException extends RuntimeException {

  private final ProcessDTO processDTO;
}
