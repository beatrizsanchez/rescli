package org.epsilonlabs.rescli.core.page;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.epsilonlabs.rescli.core.callback.AbstractCallback;
import org.epsilonlabs.rescli.core.callback.AbstractWrappedCallback;
import org.epsilonlabs.rescli.core.data.AbstractDataSet;
import org.epsilonlabs.rescli.core.data.IDataSet;
import org.epsilonlabs.rescli.core.util.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 
 * {@link AbstractPagination}
 * <p>
 * Copyright &copy; 2017 University of York.
 * @author Beatriz Sanchez
 * @version 1.0.0
 *
 */
public abstract class AbstractPagination implements IPaged {

	Integer startValue;
	Integer max;
	Integer increment;
	Integer perIteration;
	String perIterationLabel;
	String pageLabel;

	protected AbstractPagination(){}

	protected AbstractPagination(
			String pageLabel, 
			String perIterationLabel, 
			Integer perIteration,
			Integer max, 
			Integer startValue, 
			Integer increment)
	{
		set(pageLabel, perIterationLabel, perIteration, max, startValue, increment);
	}

	protected void set(
			String pageLabel, 
			String perIterationLabel, 
			Integer perIteration,
			Integer max, 
			Integer startValue, 
			Integer increment)
	{
		this.pageLabel = pageLabel;
		this.perIterationLabel = perIterationLabel;
		this.perIteration = perIteration;
		this.startValue = startValue;
		this.increment = increment;
		this.max = max;
	}

	@Override
	public Integer start() {
		return this.startValue;
	}

	@Override
	public Integer increment() {
		return this.increment;
	}

	@Override
	public Integer perIteration() {
		return this.perIteration;
	}

	@Override
	public boolean hasPerIteration() {
		return this.perIteration != null;
	}

	@Override
	public Integer max() {
		return this.max;
	}

	@Override
	public boolean hasMax() {
		return this.max != null;
	}

	@Override
	public String label() {
		return this.pageLabel;
	}

	@Override
	public String perIterationLabel() {
		return this.perIterationLabel;
	}

	/** UTILS FOR SUBCLASSES */

	protected <
		T, WRAP extends IWrap<T>, END, DATA extends AbstractDataSet<T>, CALL extends AbstractWrappedCallback<T,WRAP,DATA> 	
	> 
	IDataSet<T> traverse(
			CALL callback,			
			String methodName, 
			Class<?>[] types, 
			Object[] vals, 
			END client )
	{
		try {
			Integer start = this.start();
			Integer increment = this.increment(); 
			int pagedParams;
			Object[] listVals;
			Class<?>[] listClass;
			if (types.length != vals.length) { throw new IllegalArgumentException("Invalid parameters"); } 
			if (this.hasPerIteration()){
				pagedParams = 2;
				listVals = new Object[vals.length + pagedParams];
				listClass = new Class<?>[vals.length + pagedParams];
				listClass[vals.length] = Integer.class;
				listVals[vals.length] = this.perIteration();
			} else{
				pagedParams = 1;
				listVals = new Object[vals.length + pagedParams];
				listClass = new Class<?>[vals.length + pagedParams];
			}
			for (int i = 0 ; i < types.length ; i++ ){
				listVals[i] = vals[i];
				listClass[i] = types[i];
			}
			listClass[vals.length + pagedParams - 1] = Integer.class;
			listVals[vals.length + pagedParams - 1] = start;
			Call<WRAP> call = RetrofitUtil.<WRAP, END>getCall(methodName, listClass, listVals, (END) client);
			call.enqueue(new Callback<WRAP>() {
				@Override public void onResponse(Call<WRAP> call, Response<WRAP> response) {
					callback.handleTotal(response);
					callback.handleResponse(response);
					Integer limit = callback.totalIterations(response); // FIXME
					if (limit != null && limit != start ){
						for (int iteration = start + increment; iteration <= limit; iteration = iteration + increment){
							try {
								listVals[vals.length + pagedParams - 1] = iteration;
								Call<WRAP> pageCall = RetrofitUtil.<WRAP, END>
									getCall(methodName, listClass, listVals, client);
								pageCall.enqueue((Callback<WRAP>) callback);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							}
						}
					}
				}
				@Override public void onFailure(Call<WRAP> call, Throwable t) { t.printStackTrace(); }
			});
		} catch (Exception e) { e.printStackTrace(); }
		return callback.getDataset();		
	}
	
	
	protected <
		T, 
		END, 
		DATA extends AbstractDataSet<T>,
		CALL extends AbstractCallback<T, List<T>, DATA>
	> 
	IDataSet<T> traversePages(
			CALL callback,			
			String methodName, 
			Class<?>[] types, 
			Object[] vals, 
			END client)
	{
		try {
			Integer start = this.start();
			Integer increment = this.increment(); 

			int pagedParams;
			Object[] listVals;
			Class<?>[] listClass;

			if (types.length != vals.length) {
				throw new IllegalArgumentException("Invalid parameters");
			} 
			if (this.hasPerIteration()){
				pagedParams = 2;

				listVals = new Object[vals.length + pagedParams];
				listClass = new Class<?>[vals.length + pagedParams];

				listClass[vals.length] = Integer.class;
				listVals[vals.length] = this.perIteration();
				;
			} else{
				pagedParams = 1;
				listVals = new Object[vals.length + pagedParams];
				listClass = new Class<?>[vals.length + pagedParams];
			}
			for (int i = 0 ; i < types.length ; i++ ){
				listVals[i] = vals[i];
				listClass[i] = types[i];
			}
			listClass[vals.length + pagedParams - 1] = Integer.class;
			listVals[vals.length + pagedParams - 1] = start;

			Call<List<T>> call = RetrofitUtil.<List<T>, END>getCall(methodName, listClass, listVals, (END) client);
			call.enqueue(new Callback<List<T>>() {
				@Override public void onResponse(Call<List<T>> call, Response<List<T>> response) {
					//callback.handleTotal(response);
					//callback.handleResponse(response);
					Integer limit = callback.totalIterations(response);
					if (limit != null && limit != start ){
						for (int iteration = start + increment; iteration <= limit; iteration = iteration + increment){
							try {
								listVals[vals.length + pagedParams - 1] = iteration;
								Call<List<T>> pageCall = RetrofitUtil.<List<T>, END>
								
								getCall(methodName, listClass, listVals, client);
								
								pageCall.enqueue((Callback<List<T>>) callback);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							}
						}
					}
				}
				@Override public void onFailure(Call<List<T>> call, Throwable t) {
					t.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return callback.getDataset();		
	}
	
}
