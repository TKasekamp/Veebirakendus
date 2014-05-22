package com.codepump.data.container;

import java.util.List;

import com.codepump.data.CodeItem;

/**
 * Used to transport a list of results and expected result size. Used in browse,
 * mystuff and search pages.
 * 
 * @author TKasekamp
 * @param <T>
 *            {@link CodeItem} or {@link MyStuffListItem}
 * 
 */
public class ResultContainer<T> {
	private final int resultSize;
	private final List<T> codeList;

	public ResultContainer(int resultSize, List<T> codeList) {
		super();
		this.resultSize = resultSize;
		this.codeList = codeList;
	}

	public int getResultSize() {
		return resultSize;
	}

	public List<T> getCodeList() {
		return codeList;
	}

	@Override
	public String toString() {
		return "ResultContainer [resultSize=" + resultSize + ", codeList="
				+ codeList + "]";
	}

}
