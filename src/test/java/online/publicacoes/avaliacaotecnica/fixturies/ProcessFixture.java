package online.publicacoes.avaliacaotecnica.fixturies;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.publicacoes.avaliacaotecnica.entities.Process;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessFixture {

  public static Process getProcess() {

    return Process.builder().number(1L).defendant("Réu").archived(true).build();
  }

  public static Process getProcess(long n) {

    return Process.builder().number(n).defendant("Réu " + n).archived(true).build();
  }
}