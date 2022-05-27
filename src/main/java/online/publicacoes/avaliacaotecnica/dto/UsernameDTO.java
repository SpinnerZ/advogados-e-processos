package online.publicacoes.avaliacaotecnica.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsernameDTO {

  @NotBlank private final String username;
}
