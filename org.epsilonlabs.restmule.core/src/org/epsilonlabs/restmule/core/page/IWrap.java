package org.epsilonlabs.restmule.core.page;

/**
 * 
 * {@link IWrap}
 * <p>
 * @version 1.0.0
 *
 */
public interface IWrap<T> extends IPage<T>{

	Integer getTotalCount();
	Boolean isIncomplete();
	
}
