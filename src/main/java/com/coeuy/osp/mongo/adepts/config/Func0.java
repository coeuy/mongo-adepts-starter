package com.coeuy.osp.mongo.adepts.config;

import java.io.Serializable;


@FunctionalInterface
public interface Func0<R> extends Serializable {

	R call() throws Exception;

	default R callWithRuntimeException(){
		try {
			return call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
