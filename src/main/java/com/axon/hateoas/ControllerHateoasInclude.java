package com.axon.hateoas;

public @interface ControllerHateoasInclude {

	Class<?> resource();
		
	String method();
	
}
