package online.publicacoes.avaliacaotecnica.factories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.publicacoes.avaliacaotecnica.entities.Attorney;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AttorneyFactory {

  public static Attorney getAttorney() {

    return Attorney.builder().username("user.name").build();
  }
}
