package org.springframework.context.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.Set;

/**
 * @author derekyi
 * @date 2021/1/17
 */
public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

	private Set<?> converters;

	private GenericConversionService conversionService;

	@Override
	public void afterPropertiesSet() throws Exception {
		conversionService = new DefaultConversionService();
		registerConverters(converters, conversionService);
	}

	private void registerConverters(Set<?> converters, ConverterRegistry registry) {
		if (converters != null) {
			for (Object converter : converters) {
				if (converter instanceof GenericConverter converter2) {
					registry.addConverter(converter2);
				} else if (converter instanceof Converter<?, ?> converter1) {
					registry.addConverter(converter1);
				} else if (converter instanceof ConverterFactory<?, ?> factory) {
					registry.addConverterFactory(factory);
				} else {
					throw new IllegalArgumentException("""
							Each converter object must implement one of the \
							Converter, ConverterFactory, or GenericConverter interfaces\
							""");
				}
			}
		}
	}

	@Override
	public ConversionService getObject() throws Exception {
		return conversionService;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setConverters(Set<?> converters) {
		this.converters = converters;
	}
}
