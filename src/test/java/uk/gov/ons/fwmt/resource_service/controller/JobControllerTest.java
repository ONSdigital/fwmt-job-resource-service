package uk.gov.ons.fwmt.resource_service.controller;

import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.fwmt.resource_service.Exception.RestExceptionHandler;
import uk.gov.ons.fwmt.resource_service.data.dto.JobDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMJobEntity;
import uk.gov.ons.fwmt.resource_service.mapper.CustomObjectMapper;
import uk.gov.ons.fwmt.resource_service.service.TMJobService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class JobControllerTest {

  @Mock private TMJobService jobService;

  @Mock private MapperFacade mapperFacade;

  @InjectMocks private JobController jobController;

  private MockMvc mockMvc;

  private JobDTO jobDTO = new JobDTO();

  public static final String JOB_JSON = "{ \"tmJobId\": \"123456789\", \"lastAuthNo\": \"1276\" }";

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(jobController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()))
        .setControllerAdvice(new RestExceptionHandler())
        .build();
    MockitoAnnotations.initMocks(this);
    jobDTO = JobDTO.builder().lastAuthNo("1234").tmJobId("4567").build();
  }

  @Test
  public void getEmptyJobs() throws Exception {
    List empty = new ArrayList<TMJobEntity>();
    when(jobService.findJobs()).thenReturn(empty);
    when(mapperFacade.mapAsList(empty, JobDTO.class)).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/jobs")).andExpect(status().isOk()).andExpect(content().string("[]"));
  }

  @Test
  public void getJobs() throws Exception {
    List jobs = new ArrayList<TMJobEntity>();
    List result = new ArrayList<JobDTO>();
    result.add(jobDTO);
    when(jobService.findJobs()).thenReturn(jobs);
    when(mapperFacade.mapAsList(jobs, JobDTO.class)).thenReturn(result);
    mockMvc.perform(get("/jobs")).andExpect(status().isOk()).andExpect(jsonPath("$[0].lastAuthNo", is("1234")))
        .andExpect(jsonPath("$[0].tmJobId", is("4567")));
  }

  @Test
  public void createJob() throws Exception {
    when(jobService.findByJobId(any())).thenReturn(null);
    mockMvc.perform(post("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON)).andExpect(status().isCreated());
  }

  @Test
  public void createJobIdAlreadyUsed() throws Exception {
    when(jobService.findByJobId(any())).thenReturn(new TMJobEntity());
    mockMvc.perform(post("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON)).andExpect(status().isConflict());
  }

  @Test
  public void updateJobNotFound() throws Exception {
    when(jobService.updateJob(any())).thenReturn(null);
    mockMvc.perform(put("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void updateJob() throws Exception {
    when(jobService.updateJob(any())).thenReturn(new TMJobEntity());
    mockMvc.perform(put("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON)).andExpect(status().isOk());
  }

  @Test
  public void deleteJob() throws Exception {
    when(jobService.findByJobId(any())).thenReturn(new TMJobEntity());
    mockMvc.perform(delete("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON)).andExpect(status().isOk());
  }

  @Test
  public void deleteJobNotExist() throws Exception {
    when(jobService.findByJobId(any())).thenReturn(null);
    mockMvc.perform(delete("/jobs").contentType(MediaType.APPLICATION_JSON).content(JOB_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void getJobByJobId() throws Exception {
    when(jobService.findByJobId("1234")).thenReturn(new TMJobEntity());
    when(mapperFacade.map(any(), any())).thenReturn(jobDTO);
    mockMvc.perform(get("/jobs/1234")).andExpect(status().isOk()).andExpect(jsonPath("$.lastAuthNo", is("1234")))
        .andExpect(jsonPath("$.tmJobId", is("4567")));
  }

  @Test
  public void getJobByJobIdNotFound() throws Exception {
    when(jobService.findByJobId("1234")).thenReturn(null);
    mockMvc.perform(get("/jobs/1234")).andExpect(status().isNotFound());
  }

}
