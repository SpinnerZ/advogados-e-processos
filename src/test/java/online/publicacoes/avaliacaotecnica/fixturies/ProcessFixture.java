package online.publicacoes.avaliacaotecnica.fixturies;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.publicacoes.avaliacaotecnica.entities.Process;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessFixture {

  public static Process getProcess() {

    return Process.builder()
        .number(1L)
        .defendant("Defendant")
        .archived(false)
        .lawyer(LawyerFixture.getLawyer())
        .build();
  }

  public static Process getProcess(long n) {

    Process process = getProcess();
    process.setDefendant("Defendant " + n);

    return process;
  }
}
