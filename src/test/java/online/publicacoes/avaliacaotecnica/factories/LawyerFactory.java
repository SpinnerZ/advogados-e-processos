package online.publicacoes.avaliacaotecnica.factories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LawyerFactory {

  public static Lawyer getLawyer() {

    return Lawyer.builder().username("user.name").build();
  }
}
