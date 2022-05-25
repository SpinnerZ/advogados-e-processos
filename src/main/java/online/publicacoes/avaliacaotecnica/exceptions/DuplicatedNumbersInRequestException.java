package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import online.publicacoes.avaliacaotecnica.dto.ProcessoDTO;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class DuplicatedNumbersInRequestException extends RuntimeException {

  private final List<ProcessoDTO> processos;
}
