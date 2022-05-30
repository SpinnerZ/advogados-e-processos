package online.publicacoes.avaliacaotecnica.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsernameDTO {

  @NotBlank(message = "Lawyer username cannot be blank")
  private final String username;
}
