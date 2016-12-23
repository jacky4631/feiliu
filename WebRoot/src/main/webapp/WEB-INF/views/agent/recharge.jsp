<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
  <style>
    .btn-large-dim {
      font-size: 16px !important;
      height: 65px;
      width: 90px;
    }

    button.dim {
      margin-right: 24px;
    }

    button.btn-info.dim {
      box-shadow: 0 0 0;
    }
  </style>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-shopping-cart" sysname="${sysname}" title="流量充值"/>
    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <h5>单号码充值&nbsp;</h5>
          <span id="mobile-info">手机信息</span>
        </div>
        <div class="ibox-content">
          <div class="form-group">
            <div class="input-group">
              <span class="input-group-addon"><i class="fa fa-mobile fa-2x"></i></span>
              <input id="mobile" type="text" placeholder="输入一个手机号码" class="form-control input-lg">
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-6">
          <div class="ibox-title">
            <h5>省包产品</h5>
          </div>
          <div id="state" class="ibox-content">
            没有合适的流量包产品可供充值
          </div>
        </div>
        <div class="col-sm-6">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5>全网产品</h5>
            </div>
            <div id="nation" class="ibox-content">
              没有合适的流量包产品可供充值
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/bootbox/bootbox.min.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script>
  $(function () {
    toastr.options = {
      closeButton: true,
      progressBar: true,
      "positionClass": "toast-top-center",
      showMethod: 'slideDown',
      timeOut: 4000
    };
    packItems = function (packages, vendor, scope) {
      var htmlstr = "";
      for (var i = 0; i < packages.length; i++) {
        var pack = packages[i];
        htmlstr += "<button type='button' class='btn btn-info  dim btn-large-dim btn-outline'";
        htmlstr += "data-pack='" + pack.id + "' data-pack-title='" + vendor + pack.size + "M" + scope + "流量包'>";

        var r = pack.id.length > 8 ? "$" : "";
        htmlstr += pack.size + "M" + r + "<br>" + pack.price + "元</button>";
      }
      if (htmlstr == "") {
        return "没有合适的流量包产品可供充值";
      } else {
        return htmlstr;
      }
    };

    var showItems = function () {
      var mobile = $("#mobile").val();
      if (mobile.length == 11) {
        $.get($ctx + "/agent/packages/" + mobile + "/1",
          function (data) {
            var nation = $("#nation");
            var htmlstr = packItems(data.nationPackages, data.vendor, "全网");
            nation.html(htmlstr);

            var state = $("#state");
            htmlstr = packItems(data.statePackages, "省网", "省内");
            state.html(htmlstr);
            $('#mobile-info').html(data.mobileType);
          });
      } else {
        $("#nation,#state").html("没有合适的流量包产品可供充值");
      }
    };

    $("#m-recharge").addClass("active");
    $("#mobile").on('input', function () {
      showItems();
    });

    $("#state,#nation").on("click", 'button', function () {
      var that = this;
      var mobile = $("#mobile").val();
      var doRecharge = function () {
        var product = $(that).data("pack");
        $.ajax({
          type: "post",
          url: $ctx + "/agent/recharge",
          data: {mobile: mobile, product: product},
          success: function (data, status) {
            if (data.status == "10000") {
              toastr.success(data.message, '提交成功');
            } else {
              toastr.error(data.message, '提交失败');
            }
          },
          error: function (data, status) {
            toastr.error(data.message, '提交失败');
          }
        });
      };
      var question = "您确定要给" + mobile + "订购" + $(this).data("pack-title") + "吗?";
      bootbox.confirm(question, function (result) {
        if (result) doRecharge();
      });
    });
  });
</script>
</body>
</html>
