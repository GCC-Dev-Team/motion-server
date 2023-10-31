package com.ocj.security.domain.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class PageVO<T> implements Serializable {

   @Serial
   private static final long serialVersionUID = 1L;
   //总记录数
   private long total;
   //每页记录数
   private long pageSize;
   //总页数
   private long totalPage;
   //当前页数
   private long currPage;
   //列表数据
   private List<?> list;
   /**
    * 分页
    * @param list        列表数据
    * @param totalCount  总记录数
    * @param pageSize    每页记录数
    * @param currPage    当前页数
    */
   public PageVO(List<?> list, long totalCount, long pageSize, long currPage) {
      this.list = list;
      this.total = totalCount;
      this.pageSize = pageSize;
      this.currPage = currPage;
      this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
   }
   /**
    * 分页
    */
   public PageVO(Page<?> page) {
      this.list = page.getRecords();
      this.total = page.getTotal();
      this.pageSize = (int) page.getSize();
      this.currPage = (int) page.getCurrent();
      this.totalPage = page.getPages();
   }


}