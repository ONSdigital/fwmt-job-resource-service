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
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.resource_service.repo.TMJobRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
public class JobControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private TMJobRepo jobRepo;

  public static final String JOB_JSON = "{ \"tmJobId\": \"1234-5678\", \"lastAuthNo\": \"1276\" }";

  @Test
  public void getJobByJobIdIT() throws Exception {
    TMJobEntity jobEntity = new TMJobEntity();
    jobEntity.setTmJobId("1234-5678");
    jobEntity.setLastAuthNo("1234");
    jobRepo.save(jobEntity);
    mockMvc.perform(get("/jobs/1234-5678").with(httpBasic("user", "password"))).andExpect(status().isOk())
        .andExpect(jsonPath("$.tmJobId", is("1234-5678")))
        .andExpect(jsonPath("$.lastAuthNo", is("1234")));
  }

  @Test
  public void updateJobIT() throws Exception {
    TMJobEntity jobEntity = new TMJobEntity();
    jobEntity.setTmJobId("1234-5678");
    jobEntity.setLastAuthNo("1234");
    jobRepo.save(jobEntity);
    mockMvc.perform(
        (put("/jobs")).contentType(MediaType.APPLICATION_JSON).content(JOB_JSON).with(httpBasic("user", "password")));
    final TMJobEntity updatedJob = jobRepo.findByTmJobId("1234-5678");
    assertThat(updatedJob.getLastAuthNo()).isEqualTo("1276");
  }

  @Test
  public void deleteJobIT() throws Exception {
    TMJobEntity jobEntity = new TMJobEntity();
    jobEntity.setTmJobId("1234-5678");
    jobEntity.setLastAuthNo("1276");
    jobRepo.save(jobEntity);
    mockMvc.perform(
        delete("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON).with(httpBasic("user", "password")));
    final TMJobEntity deletedJob = jobRepo.findByTmJobId("1234-5678");
    assertThat(deletedJob).isNull();
  }

  @Test
  public void createJobIT() throws Exception {
    mockMvc.perform(
        post("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON).with(httpBasic("user", "password")));
    final TMJobEntity createdJob = jobRepo.findByTmJobId("1234-5678");
    assertThat(createdJob.getLastAuthNo()).isEqualTo("1276");
  }

  @Test
  public void getJobsIT() throws Exception {
    TMJobEntity jobEntity1 = new TMJobEntity();
    jobEntity1.setTmJobId("1234-5678");
    jobEntity1.setLastAuthNo("1234");
    jobRepo.save(jobEntity1);
    TMJobEntity jobEntity2 = new TMJobEntity();
    jobEntity2.setTmJobId("1243-8765");
    jobEntity2.setLastAuthNo("1243");
    jobRepo.save(jobEntity2);
    mockMvc.perform(get("/jobs").with(httpBasic("user", "password"))).andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  public void updateJobNotExistIT() throws Exception {
    mockMvc.perform(
        (put("/jobs")).contentType(MediaType.APPLICATION_JSON).content(JOB_JSON).with(httpBasic("user", "password"))).andExpect(status().isNotFound());
  }
}
