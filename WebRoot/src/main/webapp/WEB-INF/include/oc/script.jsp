<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script>
  $(function () {
    var url = "${ctx}/oc/tradelog/unprocess";
    $.ajax({
      type: "get",
      url: url,
      success: function (data) {
        $("#l_pending").html(data.unProcessedRecharge);
        $("#l_callback_pending").html(data.unProcessedCallback);
      }
    });
  });
</script>
