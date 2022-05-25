package online.publicacoes.avaliacaotecnica.factories;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import online.publicacoes.avaliacaotecnica.entities.Processo;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessoFactory {

  public static Processo getProcesso() {

    return Processo.builder().numero(1L).reu("Réu").ativo(true).build();
  }

  public static Processo getProcesso(long n) {

    return Processo.builder().numero(n).reu("Réu " + n).ativo(true).build();
  }
}
