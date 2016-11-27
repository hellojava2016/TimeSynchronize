<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="cn.gov.cbrc.bankriskcontrol.util.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
int current =  page.getPageNo();
int begin = Math.max(1, current - paginationSize/2);
int end = Math.min(begin + (paginationSize - 1), new Long(page.getTotalPages()).intValue());

request.setAttribute("current", current);
request.setAttribute("begin", begin);
request.setAttribute("end", end);
%>            

	<ul class="pagination">
		 <% if (page.isHasPre()){%>
              
                <li><a href="?page=${current-1}&pageSize=${page.pageSize}&sortType=${sortType}&${searchParams}"><<上一页</a></li>
         <%}else{%>
             
                <li class="disabled"><a href="#"><<上一页</a></li>
         <%} %>
         <% if(current-1 >paginationSize/2 ) {%>
         		<li><a href="?page=1&pageSize=${page.pageSize}&sortType=${sortType}&${searchParams}">1</a></li>
         		<li>
         			<em>...</em>
         		</li>
         <%} %>
 
		<c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="?page=${i}&pageSize=${page.pageSize}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${i}&pageSize=${page.pageSize}&sortType=${sortType}&${searchParams}">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
	   <% if(current + paginationSize/2 < page.getTotalPages() ) {%>
         		
         		<li>
         			<em>...</em>
         		</li>
         		<li><a href="?page=${page.totalPages}&pageSize=${page.pageSize}&sortType=${sortType}&${searchParams}">${page.totalPages}</a></li>
         <%} %>
	  	 <% if (page.isHasNext()){%>
               	<li><a href="?page=${current+1}&pageSize=${page.pageSize}&sortType=${sortType}&${searchParams}">下一页>></a></li>
               
         <%}else{%>
                <li class="disabled"><a href="#">下一页>></a></li>
             
         <%} %>
	</ul>

<div class="fenye">
                <input name="" type="text" class="fenye_lind" id="pageNo2" value="${current}"  onpaste="return false;" onpropertychange="if(isNaN(value)) value=value.substring(0,value.length-1);">
                &nbsp;/${page.totalPages}&nbsp;&nbsp;
                <input name="" type="button" value="Go" class="fenye_lindbutt_c" onclick="junpPage()"  >
              </div>
              <div class="fenye wid_250p"><span class="blue">每页显示</span>
               		<select class="fenye_control btn-group  ml_5 wid_30" id="feneeid">
                      <option value="5">5</option>
                      <option value="10">10</option>
                      <option value="50">50</option>
                   </select>
                <span class="blue">条,共${page.totalCount}条</span></div>
             <script>
             var totalpage = ${page.totalPages};
             var pageSize = ${page.pageSize};
             var urlstr ="&"+ '${searchParams}';
function junpPage(){
	var toNum = $("#pageNo2").val();
	var feneeid = $("#feneeid").val();
	if(isNaN(toNum)){
		$("#pageNo2").val(${current});
		return;
	}
	if(0==toNum){
		$("#pageNo2").val(1);
		return;
	}
	if(totalpage<toNum){
		$("#pageNo2").val(totalpage);
		return;
	}
	if(toNum==${current})
		return;
	else
		window.location.href="?page="+toNum+"&pageSize="+feneeid+urlstr;
  }
$(function (){
	$("#feneeid").val(pageSize);
	$('#feneeid').change(function(){
		var feneeid = $("#feneeid").val();
		window.location.href="?page=1&pageSize="+feneeid+urlstr;
    });
	
});	
  </script>
