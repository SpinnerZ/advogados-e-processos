package online.publicacoes.avaliacaotecnica.resources;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import online.publicacoes.avaliacaotecnica.dto.ProcessDTO;
import online.publicacoes.avaliacaotecnica.dto.ProcessFilterDTO;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import online.publicacoes.avaliacaotecnica.services.ProcessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProcessController.class)
public class ProcessControllerTest {

  @Autowired MockMvc mockMvc;
  @MockBean ProcessService service;

  String resourceRoot = "/process/";

  ProcessDTO requestDTO = ProcessFixture.getProcess().toDTO();

  @Nested
  class Post {

    @Test
    @DisplayName("Create")
    @SneakyThrows
    void createShouldReturnAProcessDTO() {

      when(service.create(requestDTO)).then(returnsFirstArg());

      mockMvc
          .perform(
              post(resourceRoot)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new Gson().toJson(requestDTO)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.number").value(requestDTO.getNumber()));
    }

    @Test
    @DisplayName("CreateAll")
    @SneakyThrows
    void createAllShouldReturnAListOfProcessDTOs() {

      ProcessDTO requestDTO2 = ProcessFixture.getProcess(2L).toDTO();
      Set<ProcessDTO> processDTOs = Set.of(requestDTO, requestDTO2);
      when(service.create(processDTOs)).thenReturn(List.of(requestDTO, requestDTO2));

      mockMvc
          .perform(
              post(resourceRoot + "mass")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new Gson().toJson(processDTOs)))
          .andExpect(status().isCreated());

      verify(service).create(anySet());
    }
  }

  @Nested
  class Get {

    @Test
    @DisplayName("RetrieveAll")
    @SneakyThrows
    void retrieveAllShouldReturnAListOfProcessDTOs() {

      when(service.retrieve()).thenReturn(List.of(requestDTO));

      mockMvc.perform(get(resourceRoot)).andExpect(status().isOk());

      verify(service).retrieve();
    }

    @Test
    @DisplayName("RetrieveOne exists")
    @SneakyThrows
    void retrieveOneShouldReturnAProcessDTOWhenItExists() {

      when(service.retrieve(requestDTO.getNumber())).thenReturn(Optional.of(requestDTO));

      mockMvc
          .perform(get(resourceRoot + "number/" + requestDTO.getNumber()))
          .andExpect(status().isOk());

      verify(service).retrieve(requestDTO.getNumber());
    }

    @Test
    @DisplayName("RetrieveOne no exists")
    @SneakyThrows
    void retrieveOneShouldReturnNotFoundWhenProcessIsNotFound() {

      when(service.retrieve(requestDTO.getNumber())).thenReturn(Optional.empty());

      mockMvc
          .perform(get(resourceRoot + "number/" + requestDTO.getNumber()))
          .andExpect(status().isNotFound());

      verify(service).retrieve(requestDTO.getNumber());
    }

    @Test
    @DisplayName("RetrieveFiltered")
    @SneakyThrows
    void retrieveFilteredShouldReturnFilteredResultList() {

      ProcessFilterDTO filter = ProcessFilterDTO.builder().archived(Boolean.FALSE).build();

      when(service.retrieve(filter)).thenReturn(List.of(requestDTO));

      mockMvc.perform(get(resourceRoot + "filter?archived=false")).andExpect(status().isOk());

      verify(service).retrieve(filter);
    }
  }

  @Nested
  class Patch {

    @Test
    @DisplayName("UpdateDefendant")
    @SneakyThrows
    void updateDefendantShouldReturnAnUpdatedProcessDTO() {

      ProcessDTO response = ProcessFixture.getProcess().toDTO();
      response.setDefendant("Slartibartfast");
      when(service.update(requestDTO.getNumber(), response.getDefendant())).thenReturn(response);

      mockMvc
          .perform(
              patch(
                  resourceRoot + requestDTO.getNumber() + "?defendant=" + response.getDefendant()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.defendant").value(response.getDefendant()));

      verify(service).update(requestDTO.getNumber(), response.getDefendant());
    }
  }

  @Nested
  class Put {

    @Test
    @DisplayName("Update")
    @SneakyThrows
    void updateShouldReturnAnUpdatedProcessDTO() {

      when(service.update(requestDTO)).then(returnsFirstArg());

      mockMvc
          .perform(
              put(resourceRoot)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new Gson().toJson(requestDTO)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.defendant").value(requestDTO.getDefendant()));

      verify(service).update(requestDTO);
    }

    @Test
    @DisplayName("Update -> Create new")
    @SneakyThrows
    void updateShouldReturnAnNewProcessDTO() {

      mockMvc
          .perform(
              put(resourceRoot)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(new Gson().toJson(requestDTO)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.defendant").value(requestDTO.getDefendant()));
    }
  }

  @Nested
  class Delete {

    @Test
    @DisplayName("Delete")
    @SneakyThrows
    void deleteShouldReturnNoContent() {

      doNothing().when(service).delete(requestDTO.getNumber());

      mockMvc
          .perform(delete(resourceRoot + requestDTO.getNumber()))
          .andExpect(status().isNoContent());
    }
  }
}
