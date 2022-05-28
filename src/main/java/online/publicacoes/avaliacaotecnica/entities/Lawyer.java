package online.publicacoes.avaliacaotecnica.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "tb_lawyer")
public class Lawyer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @EqualsAndHashCode.Include private String username;

  @OneToMany(mappedBy = "lawyer")
  private Set<Process> processes = new HashSet<>();

  public LawyerDTO toDTO() {

    return LawyerDTO.builder()
        .username(username)
        .processes(processes.stream().map(Process::toDTO).collect(Collectors.toSet()))
        .build();
  }
}
