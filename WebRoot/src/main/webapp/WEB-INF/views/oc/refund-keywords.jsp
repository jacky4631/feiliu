<%@ page import="com.jiam365.modules.utils.Collections3, java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%
  Set<String> keywords = (Set<String>) request.getAttribute("keywords");
  String list = Collections3.convertToString(keywords, "`");
  out.println("<input id=\"tags\" type=\"text\" class=\"tags\" value=\"" + list + "\"/>");
  out.println("已定义<span id='keyword_count'>" + keywords.size() + "</span>个关键字");
%>

<script type="text/javascript">
  $(function () {
    $("#tags").tagsInput({
      'height': '240px',
      'width': '100%',
      'interactive': true,
      'defaultText': '加关键字',
      'delimiter': '`',
      'removeWithBackspace': true,
      'minChars': 0,
      'maxChars': 0,
      'placeholderColor': '#666666',
      'onRemoveTag': function (tag) {
        $.ajax({
          url: $ctx + "/oc/autorefund/removekeyword",
          type: 'post',
          dataType: "json",
          data: {
            "keyword": tag
          },
          success: function (data) {
            $("#keyword_count").html(data.count);
          }
        });
      },
      'onAddTag': function (tag) {
        $.ajax({
          url: $ctx + "/oc/autorefund/addkeyword",
          type: 'post',
          dataType: "json",
          data: {
            "keyword": tag
          },
          success: function (data) {
            $("#keyword_count").html(data.count);
          }
        });
      }
    });

  });
</script>