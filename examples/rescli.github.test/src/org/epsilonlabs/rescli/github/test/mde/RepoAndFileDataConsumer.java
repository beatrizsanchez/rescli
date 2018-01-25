/*******************************************************************************
 * Copyright (c) 2017 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Konstantinos Barmpis - initial API and implementation
 ******************************************************************************/
package org.epsilonlabs.rescli.github.test.mde;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.epsilonlabs.rescli.github.model.Commits.Commit;
import org.epsilonlabs.rescli.github.model.SearchCode;
import org.epsilonlabs.rescli.github.model.SearchCode.Repository;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Trivial data consumer, printing to console
 * 
 * @author kb
 *
 */
public class RepoAndFileDataConsumer implements Observer<SearchCode> {

	HashMap<String, HashMap<String, Entry<String, HashSet<String>>>> outputMap;

	public RepoAndFileDataConsumer(HashMap<String, HashMap<String, Entry<String, HashSet<String>>>> outputMap) {
		this.outputMap = outputMap;
	}

	@Override
	public void onNext(SearchCode o) {

		System.err.println(o.getPath());

		Repository r = o.getRepository();

		String reponame = r.getFullName();

		HashMap<String, Entry<String, HashSet<String>>> files = outputMap.get(reponame);

		if (files == null)
			files = new HashMap<String, Entry<String, HashSet<String>>>();

		files.put(o.getPath(), null);
		outputMap.put(reponame, files);

		// o.getRepository().get

	}

	private String asString(Object o) {
		if (o instanceof Repository)
			return "repo: " + ((Repository) o).getFullName();
		if (o instanceof SearchCode)
			return "file: " + ((SearchCode) o).getName();
		if (o instanceof Commit)
			return "commit: " + ((Commit) o).getUrl();
		else
			return o.toString();
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();

	}

	@Override
	public void onComplete() {
		System.out.println("DATA STREAM ENDED");
		dumpData();
	}

	@Override
	public void onSubscribe(Disposable d) {
		//
	}

	//HashMap<String, HashMap<String, Entry<String, HashSet<String>>>>
	public void dumpData() {
		for (String s : outputMap.keySet()) {
			//repo
			System.out.println(s);
			for (Entry<String, Entry<String, HashSet<String>>> t : outputMap.get(s).entrySet()) {
				//file
				System.out.println("\t" + t.getKey());
				//
				System.out.println("\t\t" + t.getValue().getKey()+" : "+t.getValue().getValue());
			}
		}

	}

}
