package uk.gov.ons.fwmt.resource_service.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.ons.fwmt.resource_service.ApplicationConfig;
import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.resource_service.repo.TMUserRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ApplicationConfig.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private TMUserRepo userRepo;

  public static final String USER_JSON = "{ \"authNo\": \"1234\", \"tmUsername\": \"bla\", \"active\": true, \"alternateAuthNo\": \"7890\" }";

  @Test
  public void createUserIT() throws Exception {
    mockMvc.perform(
        post("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON).with(httpBasic("user", "password")));
    final TMUserEntity createdUser = userRepo.findByAuthNo("1234");
    assertThat(createdUser.getAlternateAuthNo()).isEqualTo("7890");
    assertThat(createdUser.getTmUsername()).isEqualTo("bla");
    assertThat(createdUser.isActive()).isTrue();
  }

  @Test
  public void updateUserIT() throws Exception {
    TMUserEntity userEntity = new TMUserEntity();
    userEntity.setActive(true);
    userEntity.setAlternateAuthNo("2345");
    userEntity.setAuthNo("1234");
    userEntity.setTmUsername("qwerty");
    userRepo.save(userEntity);

    mockMvc.perform(
        put("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON).with(httpBasic("user", "password")));

    final TMUserEntity updatedUser = userRepo.findByAuthNo("1234");
    assertThat(updatedUser.getAlternateAuthNo()).isEqualTo("7890");
    assertThat(updatedUser.getTmUsername()).isEqualTo("bla");
    assertThat(updatedUser.isActive()).isTrue();
  }

  @Test
  public void getUserByAuthNoIT() throws Exception {
    TMUserEntity userEntity = new TMUserEntity();
    userEntity.setActive(true);
    userEntity.setAlternateAuthNo("7890");
    userEntity.setAuthNo("1234");
    userEntity.setTmUsername("bla");
    userRepo.save(userEntity);

    mockMvc.perform(get("/users/auth/1234").with(httpBasic("user", "password"))).andExpect(status().isOk())
        .andExpect(jsonPath("$.authNo", is("1234")))
        .andExpect(jsonPath("$.tmUsername", is("bla")))
        .andExpect(jsonPath("$.active", is(true)))
        .andExpect(jsonPath("$.alternateAuthNo", is("7890")));

  }

  @Test
  public void getUserByAltAuthNoIT() throws Exception {
    TMUserEntity userEntity = new TMUserEntity();
    userEntity.setActive(true);
    userEntity.setAlternateAuthNo("7890");
    userEntity.setAuthNo("1234");
    userEntity.setTmUsername("bla");
    userRepo.save(userEntity);

    mockMvc.perform(get("/users/alternative/7890").with(httpBasic("user", "password"))).andExpect(status().isOk())
        .andExpect(jsonPath("$.authNo", is("1234")))
        .andExpect(jsonPath("$.tmUsername", is("bla")))
        .andExpect(jsonPath("$.active", is(true)))
        .andExpect(jsonPath("$.alternateAuthNo", is("7890")));

  }

  @Test
  public void deleteUserIT() throws Exception {
    TMUserEntity userEntity = new TMUserEntity();
    userEntity.setActive(true);
    userEntity.setAlternateAuthNo("7890");
    userEntity.setAuthNo("1234");
    userEntity.setTmUsername("bla");
    userRepo.save(userEntity);

    mockMvc.perform(
        delete("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON).with(httpBasic("user", "password")))
        .andExpect(status().isOk());

    final TMUserEntity deletedUser = userRepo.findByAuthNo("1234");
    assertThat(deletedUser).isNull();
  }

  @Test
  public void getUsersIT() throws Exception {
    TMUserEntity userEntity1 = new TMUserEntity();
    userEntity1.setActive(true);
    userEntity1.setAlternateAuthNo("7890");
    userEntity1.setAuthNo("1234");
    userEntity1.setTmUsername("bla");
    userRepo.save(userEntity1);
    TMUserEntity userEntity2 = new TMUserEntity();
    userEntity2.setActive(true);
    userEntity2.setAlternateAuthNo("7887");
    userEntity2.setAuthNo("1221");
    userEntity2.setTmUsername("qwe");
    userRepo.save(userEntity2);

    mockMvc.perform(get("/users").with(httpBasic("user", "password"))).andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  public void updateUserNotExistIT() throws Exception {
    mockMvc.perform(
        (put("/users")).contentType(MediaType.APPLICATION_JSON).content(USER_JSON).with(httpBasic("user", "password")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error", is("FWMT_RESOURCE_SERVICE_0003")))
        .andExpect(jsonPath("$.exception", is("uk.gov.ons.fwmt.resource_service.exception.FWMTException")))
        .andExpect(jsonPath("$.message", is("FWMT_RESOURCE_SERVICE_0003 - User 1234 not found")))
        .andExpect(jsonPath("$.path", is("/users")))
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.timestamp", isA(String.class)));
  }

}
