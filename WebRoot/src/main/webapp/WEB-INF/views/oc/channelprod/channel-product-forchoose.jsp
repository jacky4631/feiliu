<%@ page import="com.jiam365.flow.server.channel.FlowChannel, org.apache.commons.lang3.StringUtils, org.apache.commons.lang3.time.DateFormatUtils, org.stringtemplate.v4.ST, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%!
  String item(FlowChannel channel) {
    String template = "<li id='citem-$cid$-ALL' data-cid='$cid$' data-name='$name$' class='list-channel list-group-item'>\n" +
      "<a href='#'>\n" +
      " <small class='pull-right text-muted'>$createDate$</small>\n" +
      " <strong>$name$</strong>\n" +
      " <div class='small m-t-xs'>\n" +
      "     <p class='m-b-none'>\n" +
      "         <i class='fa fa-lightbulb-o'></i> $remark$\n" +
      "     </p>\n" +
      " </div>\n" +
      "</a>\n" +
      "</li>";
    ST st = new ST(template, '$', '$');
    st.add("cid", channel.getId());
    st.add("name", channel.getName());
    st.add("createDate", DateFormatUtils.format(channel.getCreateDate(), "yyyy-MM-dd"));
    String remark = channel.getRemark();
    if (StringUtils.isBlank(remark)) {
      remark = "无说明";
    }
    st.add("remark", remark);
    return st.render();
  }
%>
<div class="row" style="border-bottom: 1px solid #e7eaec;">
  <div class="col-sm-4" style="border-right:1px solid #e7eaec;padding:0;height: 400px; overflow-y: auto;">
    <ul id="channel_list" class="list-group elements-list">
      <%
        List<FlowChannel> channels = (List<FlowChannel>) request.getAttribute("channels");
        for (FlowChannel channel : channels) {
          out.println(item(channel));
        }
      %>
    </ul>

  </div>
  <div id="content" class="col-sm-8" style="height: 400px; overflow-y: auto;">

  </div>
</div>
<script type="text/javascript">
  $(function () {
    $("#channel_list").find("li").each(function (index) {
      var cid = $(this).data("cid");
      if ($("#" + cid + "-ALL").length > 0) {
        $(this).find("p").append("<span class='suc label pull-right label-info'>已加</span>");
      }
    })
  });
</script>