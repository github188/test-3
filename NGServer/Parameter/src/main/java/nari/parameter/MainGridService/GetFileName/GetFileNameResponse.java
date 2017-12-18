package nari.parameter.MainGridService.GetFileName;

import java.io.Serializable;

import nari.parameter.bean.SearchFile;

public class GetFileNameResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -607504655056915889L;
	
	SearchFile[] searchfile = null;

	public SearchFile[] getSearchfile() {
		return searchfile;
	}

	public void setSearchfile(SearchFile[] searchfile) {
		this.searchfile = searchfile;
	}

	
}
