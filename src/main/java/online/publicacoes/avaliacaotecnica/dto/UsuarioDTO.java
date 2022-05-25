package online.publicacoes.avaliacaotecnica.dto;

import lombok.Data;
import online.publicacoes.avaliacaotecnica.entities.Processo;
import online.publicacoes.avaliacaotecnica.entities.Usuario;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UsuarioDTO {

  @NotBlank private final String login;
  private List<ProcessoDTO> processos;

  public UsuarioDTO(Usuario usuario) {

    this.login = usuario.getLogin();
    addProcessos(usuario.getProcessos());
  }

  private void addProcessos(List<Processo> processos) {

    processos.forEach(processo -> this.processos.add(new ProcessoDTO(processo)));
  }
}
