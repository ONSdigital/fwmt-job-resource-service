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
import uk.gov.ons.fwmt.resource_service.entity.FieldPeriodEntity;
import uk.gov.ons.fwmt.resource_service.repo.FieldPeriodRepo;

import java.time.LocalDate;

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
public class FieldPeriodControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private FieldPeriodRepo fieldPeriodRepo;

  public static final String FIELD_PERIOD_JSON = "{\"startDate\": \"2018-01-12\", \"endDate\": \"2018-01-27\", \"fieldPeriod\": \"95B\"}";
  public static final String FIELD_PERIOD_UPDATE_JSON = "{\"startDate\": \"2017-01-12\", \"endDate\": \"2018-02-27\", \"fieldPeriod\": \"81A\"}";

  @Test
  public void getFieldPeriodIT() throws Exception {
    mockMvc.perform(get("/fieldPeriods/81A").with(httpBasic("user", "password"))).andExpect(status().isOk())
        .andExpect(jsonPath("$.fieldPeriod", is("81A")))
        .andExpect(jsonPath("$.endDate", is("2018-01-20")))
        .andExpect(jsonPath("$.startDate", is("2018-01-08")));
  }

  @Test
  public void postFieldPeriodIT() throws Exception {
    mockMvc.perform(post("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_JSON).with(httpBasic("user", "password")));
    final FieldPeriodEntity foundFieldPeriod = fieldPeriodRepo.findByFieldPeriod("95B");
    assertThat(foundFieldPeriod.getStartDate()).isEqualTo(LocalDate.of(2018,1,12));
    assertThat(foundFieldPeriod.getEndDate()).isEqualTo(LocalDate.of(2018,1,27));
  }

  @Test
  public void putFieldPeriodIT() throws Exception {
    mockMvc.perform(put("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_UPDATE_JSON).with(httpBasic("user", "password")));
    final FieldPeriodEntity foundFieldPeriodAfter = fieldPeriodRepo.findByFieldPeriod("81A");
    assertThat(foundFieldPeriodAfter.getStartDate()).as("Start date after update").isEqualTo(LocalDate.of(2017,1,12));
    assertThat(foundFieldPeriodAfter.getEndDate()).as("End date after update").isEqualTo(LocalDate.of(2018,2,27));
  }

  @Test
  public void deleteFieldPeriodIT() throws Exception {
    mockMvc.perform(delete("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_UPDATE_JSON).with(httpBasic("user", "password")));
    final FieldPeriodEntity foundFieldPeriodAfter = fieldPeriodRepo.findByFieldPeriod("81A");
    assertThat(foundFieldPeriodAfter).isNull();
  }

  @Test
  public void basicAuthFailureIT() throws Exception {
    mockMvc.perform(get("/fieldPeriods/81A")).andExpect(status().isUnauthorized());
  }

  @Test
  public void getAllFieldPeriodsIT() throws Exception {
    mockMvc.perform(get("/fieldPeriods").with(httpBasic("user", "password"))).andExpect(jsonPath("$", hasSize(130)));//if more fieldPeriods are added through liquibase this number will need updated
  }

  @Test
  public void updateFieldPeriodNotExistIT() throws Exception {
    mockMvc.perform(
        (put("/fieldPeriods")).contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_JSON).with(httpBasic("user", "password"))).andExpect(status().isNotFound());
  }
}
