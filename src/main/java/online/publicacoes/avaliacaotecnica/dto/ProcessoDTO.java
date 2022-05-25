package online.publicacoes.avaliacaotecnica.dto;

import lombok.Data;
import online.publicacoes.avaliacaotecnica.entities.Processo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ProcessoDTO {

  @NotNull @Positive private Integer numero;
  private String reu;

  public ProcessoDTO(Processo processo) {

    this.numero = processo.getNumero();
    this.reu = processo.getReu();
  }
}
