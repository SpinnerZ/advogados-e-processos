package online.publicacoes.avaliacaotecnica.dto;

import lombok.Data;
import online.publicacoes.avaliacaotecnica.entities.Attorney;
import online.publicacoes.avaliacaotecnica.entities.Process;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
public class AttorneyDTO {

  @NotBlank private final String username;
  private Set<ProcessDTO> processes = new HashSet<>();

  public AttorneyDTO(Attorney attorney) {

    this.username = attorney.getUsername();
    addProcessos(attorney.getProcesses());
  }

  private void addProcessos(Set<Process> processes) {

    processes.forEach(processo -> this.processes.add(new ProcessDTO(processo)));
  }
}
