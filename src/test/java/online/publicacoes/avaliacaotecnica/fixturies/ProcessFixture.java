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

    Process process =
        Process.builder()
            .number(n)
            .defendant("Defendant " + n)
            .archived(false)
            .lawyer(LawyerFixture.getLawyer())
            .build();

    process.getLawyer().setUsername("user.name" + n);

    return process;
  }
}
