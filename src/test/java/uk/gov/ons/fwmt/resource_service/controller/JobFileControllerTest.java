package uk.gov.ons.fwmt.resource_service.controller;

import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.fwmt.resource_service.exception.RestExceptionHandler;
import uk.gov.ons.fwmt.resource_service.mapper.CustomObjectMapper;
import uk.gov.ons.fwmt.resource_service.service.JobFileService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class JobFileControllerTest {

  @Mock private JobFileService jobFileService;

  @Mock private MapperFacade mapperFacade;

  @InjectMocks private JobFileController jobFileController;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(jobFileController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()))
        .setControllerAdvice(new RestExceptionHandler())
        .build();
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void storeJobFile() throws Exception {
    when(jobFileService.getJobFileByName(any())).thenReturn(null);
    MockMultipartFile file = new MockMultipartFile("file", "bla", "csv", "test".getBytes());
    mockMvc.perform(MockMvcRequestBuilders.fileUpload("/jobFile/upload").file(file).param("validated", "true")).andExpect(status().isCreated());
  }

}
