package online.publicacoes.avaliacaotecnica.resources;

import lombok.SneakyThrows;
import online.publicacoes.avaliacaotecnica.dto.LawyerDTO;
import online.publicacoes.avaliacaotecnica.fixturies.LawyerFixture;
import online.publicacoes.avaliacaotecnica.services.LawyerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LawyerController.class)
class LawyerControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean LawyerService service;

  String resourceRoot = "/lawyer/";
  String username = "user.name";
  LawyerDTO lawyerDTO;

  @BeforeEach
  void setUp() {

    lawyerDTO = LawyerFixture.getLawyer().toDTO();
  }

  @Test
  @DisplayName("Post")
  @SneakyThrows
  void createShouldReturnCreated() {

    when(service.create(username)).thenReturn(lawyerDTO);

    mockMvc
        .perform(post(resourceRoot + username))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value(username));
  }

  @Test
  @DisplayName("Get")
  @SneakyThrows
  void getShouldReturnALawyer() {

    when(service.retrieve(username)).thenReturn(Optional.of(lawyerDTO));

    mockMvc
        .perform(get(resourceRoot + username))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username));
  }

  @Test
  @DisplayName("Patch")
  @SneakyThrows
  void patchShouldReturnOk() {

    when(service.update(username, "new.user.name")).thenReturn(lawyerDTO);

    mockMvc
        .perform(patch(resourceRoot + username + "?new_username=new.user.name"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value(username));
  }

  @Test
  @DisplayName("Delete")
  @SneakyThrows
  void deleteShouldReturnNoContent() {

    doNothing().when(service).delete(username);

    mockMvc.perform(delete(resourceRoot + username)).andExpect(status().isNoContent());
  }
}
