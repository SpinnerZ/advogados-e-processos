package online.publicacoes.avaliacaotecnica.fixturies;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;

import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LawyerFixture {

  public static Lawyer getLawyer() {

    return Lawyer.builder().username("user.name").processes(new HashSet<>()).build();
  }
}
