package net.wanho.util;

public class PageBean {
	private int page;
	private int pageSize;
	private int allPage;
	private int start;
	private int end;
	public PageBean() {
		super();
	}
	public PageBean(int page, int pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStart() {
		this.start=(this.page-1)*this.pageSize;
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		this.end=this.page*this.pageSize;
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public int getAllPage() {
		return allPage;
	}
	public void setAllPage(int allPage) {
		this.allPage = allPage;
	}
	
}
