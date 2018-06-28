package uk.gov.ons.fwmt.resource_service.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class BeanMapper extends ConfigurableMapper {

  @Override
  public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
    builder.compilerStrategy(new EclipseJdtCompilerStrategy());
  }

  @Override
  protected final void configure(final MapperFactory factory) {
    factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDate.class));
    factory.getConverterFactory().registerConverter(new PassThroughConverter(LocalDateTime.class));


  }

}
