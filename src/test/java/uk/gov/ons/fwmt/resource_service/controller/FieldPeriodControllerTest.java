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
import uk.gov.ons.fwmt.resource_service.data.dto.FieldPeriodDTO;
import uk.gov.ons.fwmt.resource_service.entity.FieldPeriodEntity;
import uk.gov.ons.fwmt.resource_service.exception.RestExceptionHandler;
import uk.gov.ons.fwmt.resource_service.mapper.CustomObjectMapper;
import uk.gov.ons.fwmt.resource_service.service.FieldPeriodService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class FieldPeriodControllerTest {

  @Mock FieldPeriodService fieldPeriodService;

  @Mock private MapperFacade mapperFacade;

  @InjectMocks private FieldPeriodController fieldPeriodController;

  private MockMvc mockMvc;

  private FieldPeriodDTO fieldPeriodDTO = new FieldPeriodDTO();

  public static final String FIELD_PERIOD_JSON = "{\"startDate\": \"2018-01-12\", \"endDate\": \"2018-01-27\", \"fieldPeriod\": \"95B\"}";
  public static final String FIELD_PERIOD_UPDATE_JSON = "{\"startDate\": \"2017-01-12\", \"endDate\": \"2018-02-27\", \"fieldPeriod\": \"81A\"}";

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(fieldPeriodController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()))
        .setControllerAdvice(new RestExceptionHandler())
        .build();
    MockitoAnnotations.initMocks(this);
    fieldPeriodDTO = FieldPeriodDTO.builder().fieldPeriod("88B").endDate(LocalDate.of(2018, 11, 15))
        .startDate(LocalDate.of(2017, 11, 16)).build();
  }

  @Test
  public void getAllFieldPeriods() throws Exception {
    List fieldPeriods = new ArrayList<FieldPeriodEntity>();
    List result = new ArrayList<FieldPeriodDTO>();
    result.add(fieldPeriodDTO);
    when(fieldPeriodService.findFieldPeriods()).thenReturn(fieldPeriods);
    when(mapperFacade.mapAsList(fieldPeriods, FieldPeriodDTO.class)).thenReturn(result);
    mockMvc.perform(get("/fieldPeriods")).andExpect(status().isOk()).andExpect(jsonPath("$[0].fieldPeriod", is("88B")))
        .andExpect(jsonPath("$[0].endDate", is("2018-11-15"))).andExpect(jsonPath("$[0].startDate", is("2017-11-16")));
  }

  @Test
  public void getFieldPeriod() throws Exception {
    when(fieldPeriodService.findFieldPeriod("88B")).thenReturn(new FieldPeriodEntity());
    when(mapperFacade.map(any(), any())).thenReturn(fieldPeriodDTO);
    mockMvc.perform(get("/fieldPeriods/88B")).andExpect(status().isOk()).andExpect(jsonPath("$.fieldPeriod", is("88B")))
        .andExpect(jsonPath("$.endDate", is("2018-11-15"))).andExpect(jsonPath("$.startDate", is("2017-11-16")));
  }

  @Test
  public void getFieldPeriodNotFound() throws Exception {
    when(fieldPeriodService.findFieldPeriod("88B")).thenReturn(null);
    mockMvc.perform(get("/fieldPeriods/88B")).andExpect(status().isNotFound());
  }

  @Test
  public void createFieldPeriod() throws Exception {
    when(fieldPeriodService.findFieldPeriod("95B")).thenReturn(null);
    mockMvc.perform(post("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_JSON)).andExpect(status().isCreated());
  }

  @Test
  public void createFieldPeriodAlreadyExists() throws Exception {
    when(fieldPeriodService.findFieldPeriod("95B")).thenReturn(new FieldPeriodEntity());
    mockMvc.perform(post("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_JSON)).andExpect(status().isConflict());
  }

  @Test
  public void updateFieldPeriod() throws Exception {
    when(fieldPeriodService.updateFieldPeriod(any())).thenReturn(new FieldPeriodEntity());
    mockMvc.perform(put("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_UPDATE_JSON)).andExpect(status().isOk());
  }

  @Test
  public void updateFieldPeriodNotFound() throws Exception {
    when(fieldPeriodService.findFieldPeriod(any())).thenReturn(null);
    mockMvc.perform(put("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_UPDATE_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void deleteFieldPeriod() throws Exception {
    when(fieldPeriodService.findFieldPeriod(any())).thenReturn(new FieldPeriodEntity());
    mockMvc.perform(delete("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_JSON)).andExpect(status().isOk());
  }

  @Test
  public void deleteFieldPeriodNotFound() throws Exception {
    when(fieldPeriodService.findFieldPeriod(any())).thenReturn(null);
    mockMvc.perform(delete("/fieldPeriods").contentType(MediaType.APPLICATION_JSON).content(FIELD_PERIOD_JSON)).andExpect(status().isNotFound());
  }
}