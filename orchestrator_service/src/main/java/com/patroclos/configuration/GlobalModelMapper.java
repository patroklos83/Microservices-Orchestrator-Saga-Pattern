package com.patroclos.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class GlobalModelMapper {

	private static ModelMapper modelMapper;

	static {}

	public static ModelMapper getModelMapper() {
		if (modelMapper == null) {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration()
			.setFieldMatchingEnabled(true)
			.setMatchingStrategy(MatchingStrategies.STRICT)
			.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
			GlobalModelMapper.modelMapper = modelMapper;
			return modelMapper;
		}
		else
			return modelMapper;
	}

}
