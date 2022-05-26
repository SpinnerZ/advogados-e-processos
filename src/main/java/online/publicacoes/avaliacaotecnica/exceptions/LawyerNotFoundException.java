package online.publicacoes.avaliacaotecnica.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LawyerNotFoundException extends RuntimeException {

  private final String username;
}
