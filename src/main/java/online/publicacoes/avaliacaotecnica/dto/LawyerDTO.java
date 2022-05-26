package online.publicacoes.avaliacaotecnica.dto;

import lombok.Data;
import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import online.publicacoes.avaliacaotecnica.entities.Process;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
public class LawyerDTO {

  @NotBlank private final String username;
  private Set<ProcessDTO> processes = new HashSet<>();

  public LawyerDTO(Lawyer lawyer) {

    this.username = lawyer.getUsername();
    addProcesses(lawyer.getProcesses());
  }

  private void addProcesses(Set<Process> processes) {

    processes.forEach(process -> this.processes.add(new ProcessDTO(process)));
  }
}
