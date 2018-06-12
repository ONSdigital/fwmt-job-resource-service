package uk.gov.ons.fwmt.resource_service.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.ons.fwmt.resource_service.ApplicationConfig;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class FieldPeriodIT {

  @Autowired private MockMvc mockMvc;

  @Test
  public void getFieldPeriodIT() throws Exception {
    mockMvc.perform(get("/fieldperiods/81A").with(httpBasic("user", "password"))).andExpect(status().isOk())
        .andExpect(jsonPath("$.fieldPeriod", is("81A")))
        .andExpect(jsonPath("$.endDate", is("2018-01-20")))
        .andExpect(jsonPath("$.startDate", is("2018-01-08")));
  }

  @Test
  public void basicAuthFailureIT() throws Exception {
    mockMvc.perform(get("/fieldperiods/81A")).andExpect(status().isUnauthorized());
  }
}
