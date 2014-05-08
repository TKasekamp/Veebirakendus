package com.codepump.tempobject;

import java.util.List;

import com.codepump.data.CodeItem;

/**
 * Used to transport search results and expected result size.
 * 
 * @author TKasekamp
 * 
 */
public class SearchContainer {
	int resultSize;
	List<CodeItem> codeList;

	public SearchContainer(int resultSize, List<CodeItem> codeList) {
		super();
		this.resultSize = resultSize;
		this.codeList = codeList;
	}

	public SearchContainer() {
	}

	public int getResultSize() {
		return resultSize;
	}

	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public List<CodeItem> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CodeItem> codeList) {
		this.codeList = codeList;
	}

	@Override
	public String toString() {
		return "SearchContainer [resultSize=" + resultSize + ", codeList="
				+ codeList + "]";
	}

}
