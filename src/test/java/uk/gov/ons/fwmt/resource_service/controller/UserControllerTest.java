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
import uk.gov.ons.fwmt.resource_service.data.dto.UserDTO;
import uk.gov.ons.fwmt.resource_service.entity.TMUserEntity;
import uk.gov.ons.fwmt.resource_service.mapper.CustomObjectMapper;
import uk.gov.ons.fwmt.resource_service.service.TMUserService;

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
public class UserControllerTest {

  @Mock
  private TMUserService userService;

  @Mock
  private MapperFacade mapperFacade;

  @InjectMocks
  private UserController userController;

  private MockMvc mockMvc;

  private UserDTO userDTO = new UserDTO();

  public static final String USER_JSON = "{ \"authNo\": \"1234\", \"tmUsername\": \"bla\", \"active\": true, \"alternateAuthNo\": \"7890\" }";

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()))
        .setControllerAdvice(new RestExceptionHandler())
        .build();
    MockitoAnnotations.initMocks(this);
    userDTO = UserDTO.builder().authNo("1234").tmUsername("bla").active(true).alternateAuthNo("7890").build();
  }

  @Test
  public void getEmptyUsers() throws Exception {
    List empty = new ArrayList<TMUserEntity>();
    when(userService.findUsers()).thenReturn(empty);
    when(mapperFacade.mapAsList(empty, UserDTO.class)).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(content().string("[]"));
  }

  @Test
  public void getUsers() throws Exception {
    List users = new ArrayList<TMUserEntity>();
    List result = new ArrayList<UserDTO>();
    result.add(userDTO);
    when(userService.findUsers()).thenReturn(users);
    when(mapperFacade.mapAsList(users, UserDTO.class)).thenReturn(result);
    mockMvc.perform(get("/users")).andExpect(status().isOk()).andExpect(jsonPath("$[0].authNo", is("1234")))
        .andExpect(jsonPath("$[0].tmUsername", is("bla"))).andExpect(jsonPath("$[0].active", is(true)));
  }

  @Test
  public void createUser() throws Exception {
    when(userService.findUserAuthNo(any())).thenReturn(null);
    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON)).andExpect(status().isCreated());
  }

  @Test
  public void createUserAuthNoAlreadyUsed() throws Exception {
    when(userService.findUserAuthNo(any())).thenReturn(new TMUserEntity());
    mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON)).andExpect(status().isConflict());
  }

  @Test
  public void updateUserNotFound() throws Exception {
    when(userService.updateUser(any())).thenReturn(null);
    mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void updateUser() throws Exception {
    when(userService.updateUser(any())).thenReturn(new TMUserEntity());
    mockMvc.perform(put("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON)).andExpect(status().isOk());
  }

  @Test
  public void deleteUser() throws Exception {
    when(userService.findUserAuthNo(any())).thenReturn(new TMUserEntity());
    mockMvc.perform(delete("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON)).andExpect(status().isOk());
  }

  @Test
  public void deleteUserNotExist() throws Exception {
    when(userService.findUserAuthNo(any())).thenReturn(null);
    mockMvc.perform(delete("/users").contentType(MediaType.APPLICATION_JSON).content(USER_JSON)).andExpect(status().isNotFound());
  }

  @Test
  public void getUserByAuthNo() throws Exception {
    when(userService.findUserAuthNo("1234")).thenReturn(new TMUserEntity());
    when(mapperFacade.map(any(), any())).thenReturn(userDTO);
    mockMvc.perform(get("/users/auth/1234")).andExpect(status().isOk()).andExpect(jsonPath("$.authNo", is("1234")))
        .andExpect(jsonPath("$.tmUsername", is("bla"))).andExpect(jsonPath("$.active", is(true)));
  }

  @Test
  public void getUserByAuthNoNotFound() throws Exception {
    when(userService.findUserAuthNo("1234")).thenReturn(null);
    mockMvc.perform(get("/users/auth/1234")).andExpect(status().isNotFound());
  }

  @Test
  public void getUserByAltAuthNo() throws Exception {
    when(userService.findUserAlternateAuthNo("1234")).thenReturn(new TMUserEntity());
    when(mapperFacade.map(any(), any())).thenReturn(userDTO);
    mockMvc.perform(get("/users/alternative/1234")).andExpect(status().isOk()).andExpect(jsonPath("$.authNo", is("1234")))
        .andExpect(jsonPath("$.tmUsername", is("bla"))).andExpect(jsonPath("$.active", is(true)));
  }

  @Test
  public void getUserByAltAuthNoNotFound() throws Exception {
    when(userService.findUserAlternateAuthNo("1234")).thenReturn(null);
    mockMvc.perform(get("/users/alternative/1234")).andExpect(status().isNotFound());
  }
}