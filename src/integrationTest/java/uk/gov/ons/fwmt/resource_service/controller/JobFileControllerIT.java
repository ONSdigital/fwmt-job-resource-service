package uk.gov.ons.fwmt.resource_service.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.ons.fwmt.resource_service.ApplicationConfig;
import uk.gov.ons.fwmt.resource_service.entity.JobFileEntity;
import uk.gov.ons.fwmt.resource_service.repo.JobFileEntityRepo;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ApplicationConfig.class)
@AutoConfigureMockMvc
@Transactional
public class JobFileControllerIT {

  @Autowired private MockMvc mockMvc;

  @Autowired private JobFileEntityRepo jobFileEntityRepo;

  @Test
  public void testUploadFile() throws Exception {
    FileInputStream fis = new FileInputStream("src/integrationTest/resources/sample_GFF_2018-06-28T16-00-00Z.csv");
    final String filename = "sample_GFF_2018-06-28T16-00-00Z.csv";
    MockMultipartFile multipartFile = new MockMultipartFile("file", filename, "application/csv", fis);
    mockMvc.perform(
        MockMvcRequestBuilders.fileUpload("/jobFile/upload").file(multipartFile).param("validated", "true").with(httpBasic("user", "password")))
        .andExpect(status().isCreated());

    final Optional<JobFileEntity> jobFileEntity = jobFileEntityRepo.findByfilename(filename);
    assertThat(jobFileEntity.get().getFileTime(), is(LocalDateTime.of(2018, 06, 28, 16, 00, 00)));
    assertThat(jobFileEntity.get().isValidated(), is(true));
  }

    @Test
    public void getJobFileByfileNameShouldReturnOkIfSucessful() throws Exception {
        JobFileEntity jobFileEntity = new JobFileEntity();
        jobFileEntity.setFile(new byte[10]);
        jobFileEntity.setFilename("fileTest");
        jobFileEntityRepo.save(jobFileEntity);
        mockMvc.perform(get("/jobFile/fileTest").with(httpBasic("user", "password"))).andExpect(status().isOk());
    }

    @Test
    public void getJobFileByfileNameShouldReturn404IfNotFound() throws Exception {
        JobFileEntity jobFileEntity = new JobFileEntity();
        jobFileEntity.setFile(new byte[10]);
        jobFileEntity.setFilename("fileTest");
        jobFileEntityRepo.save(jobFileEntity);
        mockMvc.perform(get("/jobFile/fileNotFound").with(httpBasic("user", "password"))).andExpect(status().isNotFound());
    }
}
